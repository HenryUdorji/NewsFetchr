package com.henryudorji.newsfetchr.database

import androidx.room.TypeConverter
import com.henryudorji.newsfetchr.model.Source

//
// Created by  on 2/26/2021.
//
class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}