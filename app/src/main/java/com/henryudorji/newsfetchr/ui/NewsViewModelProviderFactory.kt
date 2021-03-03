package com.henryudorji.newsfetchr.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.henryudorji.newsfetchr.repository.NewsRepository

//
// Created by  on 2/26/2021.
//
class NewsViewModelProviderFactory(
    private val application: Application,
    private val newsRepository: NewsRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NewsViewModel(application, newsRepository) as T
    }
}