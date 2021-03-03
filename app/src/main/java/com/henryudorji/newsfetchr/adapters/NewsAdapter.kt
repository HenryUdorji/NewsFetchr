package com.henryudorji.newsfetchr.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.henryudorji.newsfetchr.R
import com.henryudorji.newsfetchr.databinding.NewsListLayoutBinding
import com.henryudorji.newsfetchr.model.Article
import com.henryudorji.newsfetchr.utils.Utils

//
// Created by  on 2/26/2021.
//
class NewsAdapter: RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(var binding: NewsListLayoutBinding): RecyclerView.ViewHolder(binding.root)

    private val differCallback = object: DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsListLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = differ.currentList[position];

        holder.binding.apply {
            Glide.with(this.root)
                .load(article.urlToImage)
                .placeholder(R.drawable.placeholder_1)
                .into(articleImage)

            articleTitle.text = article.title
            articleDescription.text = article.description
            articleAuthor.text = article.author
            articlePublishedAt.text = Utils.formatDate(article.publishedAt)
            articleSource.text = article.source?.name

            this.root.setOnClickListener {
                onItemClickListener?.let { it(article) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit) ? = null

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}