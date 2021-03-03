package com.henryudorji.newsfetchr.repository

import com.henryudorji.newsfetchr.api.ServiceGenerator
import com.henryudorji.newsfetchr.database.ArticleDatabase
import com.henryudorji.newsfetchr.model.Article
import retrofit2.Retrofit

class NewsRepository(
    private val database: ArticleDatabase
) {
    suspend fun getBreakingNews(page: Int) = ServiceGenerator.api.getBreakingNews(page)

    suspend fun searchNews(searchQuery: String, page: Int) = ServiceGenerator.api.searchForNews(searchQuery, page)

    suspend fun insertArticle(article: Article) = database.getArticleDao().insertArticle(article)

    fun getSavedArticle() = database.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = database.getArticleDao().deleteArticle(article)

    suspend fun deleteAllArticle() = database.getArticleDao().deleteAllArticle()

}
