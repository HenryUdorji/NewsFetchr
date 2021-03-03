package com.henryudorji.newsfetchr.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.henryudorji.newsfetchr.model.Article

//
// Created by  on 2/25/2021.
//

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("DELETE FROM articles")
    suspend fun deleteAllArticle();
}