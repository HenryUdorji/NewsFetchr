package com.henryudorji.newsfetchr.utils

//
// Created by  on 2/25/2021.
//
class Constants {

    companion object {
        const val PACKAGE = "com.henryudorji.newsfetchr"
        const val API_KEY = "023fc968a975442394b5fde7fe0bb7e5";
        const val BASE_URL = "https://newsapi.org/"
        const val ARTICLE = "article"
        const val QUERY_PAGE_SIZE = 20

        //shared pref keys
        const val COUNTRY = "${PACKAGE}_country"
        const val CATEGORY = "${PACKAGE}_category"
        //shared pref values
        //countries
        const val USA = "us"
        const val NIGERIA = "ng"
        const val INDIA = "in"
        const val UK = "uk"
        const val CHINA = "cn"
        const val SOUTH_AFRICA = "za"
        //categories
        const val BUSINESS = "business"
        const val GENERAL = "general"
        const val SPORTS = "sports"
        const val HEALTH = "health"
        const val ENTERTAINMENT = "entertainment"
        const val TECHNOLOGY = "technology"

    }
}