package com.henryudorji.newsfetchr.ui.fragments

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.henryudorji.newsfetchr.R
import com.henryudorji.newsfetchr.databinding.FragmentArticleBinding
import com.henryudorji.newsfetchr.ui.MainActivity
import com.henryudorji.newsfetchr.ui.NewsViewModel

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel
        (activity as MainActivity).supportActionBar?.title = "Article"

        val article = args.article
        val webViewClient: WebViewClient = object: WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return false
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.progressBar.visibility = View.VISIBLE
                binding.archiveArticleFab.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBar.visibility = View.GONE
                binding.archiveArticleFab.visibility = View.VISIBLE
            }
        }
        binding.articleWebView.webViewClient = webViewClient
        binding.articleWebView.loadUrl(article.url!!)

        binding.archiveArticleFab.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(binding.root, "Article saved to Archive", Snackbar.LENGTH_SHORT).show()
        }
    }
}