package com.henryudorji.newsfetchr.ui.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.newsfetchr.R
import com.henryudorji.newsfetchr.adapters.NewsAdapter
import com.henryudorji.newsfetchr.databinding.FragmentArchiveBinding
import com.henryudorji.newsfetchr.ui.MainActivity
import com.henryudorji.newsfetchr.ui.NewsViewModel
import com.henryudorji.newsfetchr.utils.Constants

class ArchiveFragment : Fragment(R.layout.fragment_archive) {

    private lateinit var binding: FragmentArchiveBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArchiveBinding.bind(view)
        setHasOptionsMenu(true)

        viewModel = (activity as MainActivity).viewModel
        (activity as MainActivity).supportActionBar?.title = "Archive"
        setupRecyclerView()
        getSavedArticleList()

        //ItemTouchHelperCallback
        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(binding.root, "Article deleted successfully", Snackbar.LENGTH_LONG).apply {
                    setAction("undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.recyclerView)
        }
    }

    private fun getSavedArticleList() {
        viewModel.getSavedArticle().observe(viewLifecycleOwner, Observer { articles ->
            if (articles.isNotEmpty()) {
                binding.networkMessage.visibility = View.GONE
                newsAdapter.differ.submitList(articles)
            }else {
                binding.networkMessage.visibility = View.VISIBLE
            }
        })
    }

    //TODO -> {When all Articles are deleted from the db the viewModel still retains the already
    // deleted data, have to make the viewModel not retain the data.}
    private fun showAlertDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete all Archive")
            setMessage("You cannot undo this action")
            setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }
            setPositiveButton("Delete all"){ dialog, which ->
                viewModel.deleteAllArticle()
                getSavedArticleList()
                newsAdapter.notifyDataSetChanged()
                dialog.dismiss()
                Snackbar.make(binding.root, "All Articles deleted successfully", Snackbar.LENGTH_SHORT).show()
            }
        }.show()
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable(Constants.ARTICLE, it)
            }
            findNavController().navigate(R.id.action_archiveFragment_to_articleFragment, bundle)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        menu.findItem(R.id.filter).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll) {
            showAlertDialog()
        }
        return super.onOptionsItemSelected(item)
    }

}