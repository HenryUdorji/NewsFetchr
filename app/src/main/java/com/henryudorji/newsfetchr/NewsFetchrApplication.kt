package com.henryudorji.newsfetchr

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager

//
// Created by  on 2/27/2021.
//
class NewsFetchrApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        sharedPref = PreferenceManager.getDefaultSharedPreferences(applicationContext)
    }

    companion object {
        lateinit var sharedPref: SharedPreferences
    }


}