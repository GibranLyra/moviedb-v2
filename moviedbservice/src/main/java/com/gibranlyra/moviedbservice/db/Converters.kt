package com.gibranlyra.moviedbservice.db

import androidx.room.TypeConverter
import com.gibranlyra.moviedbservice.model.Images
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    @TypeConverter
    fun restoreList(listOfString: String): ArrayList<String>? {
        return Gson().fromJson(listOfString, object : TypeToken<ArrayList<String>?>() {}.type)
    }

    @TypeConverter
    fun saveList(listOfString: ArrayList<String>?): String {
        return Gson().toJson(listOfString)
    }

    @TypeConverter
    fun restoreImages(imagesString: String): Images {
        return Gson().fromJson(imagesString, object : TypeToken<Images>() {}.type)
    }

    @TypeConverter
    fun saveImages(listOfString: Images): String {
        return Gson().toJson(listOfString)
    }
}