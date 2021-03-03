package com.henryudorji.newsfetchr.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.newsfetchr.R
import com.henryudorji.newsfetchr.adapters.NewsAdapter
import com.henryudorji.newsfetchr.databinding.DialogFilterLayoutBinding
import com.henryudorji.newsfetchr.databinding.FragmentBreakingNewsBinding
import com.henryudorji.newsfetchr.ui.MainActivity
import com.henryudorji.newsfetchr.ui.NewsViewModel
import com.henryudorji.newsfetchr.utils.*
import com.henryudorji.newsfetchr.utils.Constants.Companion.ARTICLE
import com.henryudorji.newsfetchr.utils.Constants.Companion.QUERY_PAGE_SIZE

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private val TAG = "BreakingNewsFragment"
    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBreakingNewsBinding.bind(view)
        setHasOptionsMenu(true)

        viewModel = (activity as MainActivity).viewModel
        (activity as MainActivity).supportActionBar?.title = "Breaking News"
        setupRecyclerView()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideNoNetworkView()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                        showNoNetworkView(message)
                        Log.e(TAG, "ERROR ->: $message")
                    }
                }
                is Resource.Loading -> {
                    hideNoNetworkView()
                    showProgressBar()
                }
            }
        })
    }

    private fun hideNoNetworkView() {
        binding.noNetworkLayout.visibility = View.GONE
    }

    private fun showNoNetworkView(message: String) {
        binding.noNetworkLayout.visibility = View.VISIBLE
        binding.networkMessage.text = message
        binding.retryBtn.setOnClickListener {
            viewModel.getBreakingNews()
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        }
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(ARTICLE, it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }
    }


    // Paginating the recyclerView
    var isScrolling = false
    var isLoading = false
    var isLastPage = false

    private val scrollListener = object: RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotAtLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate = isNotLoadingAndNotAtLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews()
                isScrolling = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        menu.findItem(R.id.deleteAll).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter) {
            showFilterDialog()
            //FilterDialog(requireContext(), viewModel).show()
        }
        return super.onOptionsItemSelected(item)
    }

    //TODO -> {The options selected from the dialog are not reflecting any change to the data on
    // the viewModel. I don't know how to fix that yet}
    private fun showFilterDialog() {
        val dialog = Dialog(requireContext())
        val binding: DialogFilterLayoutBinding = DialogFilterLayoutBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.firstRG.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                binding.countryRB.id -> {
                    binding.countryRG.visibility = View.VISIBLE
                    binding.categoryRG.visibility = View.GONE
                }
                binding.categoryRB.id -> {
                    binding.categoryRG.visibility = View.VISIBLE
                    binding.countryRG.visibility = View.GONE
                }
            }
        }

        binding.countryRG.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                binding.usaRB.id -> {
                    binding.usaRB.isChecked = true
                    SharedPref.putStringInPref(Constants.COUNTRY, Constants.USA)
                }
                binding.ukRB.id -> {
                    binding.ukRB.isChecked = true
                    SharedPref.putStringInPref(Constants.COUNTRY, Constants.UK)
                }
                binding.nigeriaRB.id -> {
                    binding.nigeriaRB.isChecked = true
                    SharedPref.putStringInPref(Constants.COUNTRY, Constants.NIGERIA)
                }
                binding.indiaRB.id -> {
                    binding.indiaRB.isChecked = true
                    SharedPref.putStringInPref(Constants.COUNTRY, Constants.INDIA)
                }
                binding.chinaRB.id -> {
                    binding.chinaRB.isChecked = true
                    SharedPref.putStringInPref(Constants.COUNTRY, Constants.CHINA)
                }
            }
        }

        binding.categoryRG.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                binding.businessRB.id -> {
                    binding.businessRB.isChecked = true
                    SharedPref.putStringInPref(Constants.CATEGORY, Constants.BUSINESS)
                }
                binding.generalRB.id -> {
                    binding.generalRB.isChecked = true
                    SharedPref.putStringInPref(Constants.CATEGORY, Constants.GENERAL)
                }
                binding.sportsRB.id -> {
                    binding.sportsRB.isChecked = true
                    SharedPref.putStringInPref(Constants.CATEGORY, Constants.SPORTS)
                }
                binding.healthRB.id -> {
                    binding.healthRB.isChecked = true
                    SharedPref.putStringInPref(Constants.CATEGORY, Constants.HEALTH)
                }
                binding.entertainmentRB.id -> {
                    binding.entertainmentRB.isChecked = true
                    SharedPref.putStringInPref(Constants.CATEGORY, Constants.ENTERTAINMENT)
                }
                binding.technologyRB.id -> {
                    binding.technologyRB.isChecked = true
                    SharedPref.putStringInPref(Constants.CATEGORY, Constants.TECHNOLOGY)
                }
            }
        }

        fun checkAndSetCountryRG() {
            when(SharedPref.getCountryFromPref()) {
                Constants.USA -> binding.usaRB.isChecked = true
                Constants.NIGERIA -> binding.nigeriaRB.isChecked = true
                Constants.UK -> binding.ukRB.isChecked = true
                Constants.INDIA -> binding.indiaRB.isChecked = true
                Constants.CHINA -> binding.chinaRB.isChecked = true
            }
        }

        fun checkAndSetCategoryRG() {
            when(SharedPref.getCategoryFromPref()) {
                Constants.BUSINESS -> binding.businessRB.isChecked = true
                Constants.GENERAL -> binding.generalRB.isChecked = true
                Constants.SPORTS -> binding.sportsRB.isChecked = true
                Constants.HEALTH -> binding.healthRB.isChecked = true
                Constants.ENTERTAINMENT -> binding.entertainmentRB.isChecked = true
                Constants.TECHNOLOGY -> binding.technologyRB.isChecked = true
            }
        }

        dialog.onAttachedToWindow().apply {
            checkAndSetCountryRG()
            checkAndSetCategoryRG()
        }
        /*dialog.onDetachedFromWindow().apply {
            viewModel.getBreakingNews()
            requireActivity().recreate()
        }*/
        dialog.show()
    }

}