package com.henryudorji.newsfetchr.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import com.henryudorji.newsfetchr.R
import com.henryudorji.newsfetchr.databinding.DialogFilterLayoutBinding
import java.text.SimpleDateFormat

//
// Created by  on 2/28/2021.
//
class Utils() {

    companion object {
        fun formatDate(date: String?): String? {
            val simpleDateFormat = SimpleDateFormat("E, d MMM yyyy")
            val simpleDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date)
            return simpleDateFormat.format(simpleDate)
        }
    }
}