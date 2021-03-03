package com.henryudorji.newsfetchr.utils

import android.content.SharedPreferences
import com.henryudorji.newsfetchr.NewsFetchrApplication
import com.henryudorji.newsfetchr.utils.Constants.Companion.CATEGORY
import com.henryudorji.newsfetchr.utils.Constants.Companion.COUNTRY
import com.henryudorji.newsfetchr.utils.Constants.Companion.GENERAL
import com.henryudorji.newsfetchr.utils.Constants.Companion.USA

//
// Created by  on 2/28/2021.
//
class SharedPref {

    companion object {

        fun putStringInPref(key: String, value: String) {
            val editor = NewsFetchrApplication.sharedPref.edit()
            editor.putString(key, value)
            editor.apply()
        }

        fun getCountryFromPref(): String {
            return NewsFetchrApplication.sharedPref.getString(COUNTRY, USA)!!
        }

        fun getCategoryFromPref(): String {
            return NewsFetchrApplication.sharedPref.getString(CATEGORY, GENERAL)!!
        }
    }
}