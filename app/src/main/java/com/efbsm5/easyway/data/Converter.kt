package com.efbsm5.easyway.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromPhotoList(photos: List<Photo>): String {
        return Gson().toJson(photos)
    }

    @TypeConverter
    fun toPhotoList(photosString: String): List<Photo> {
        val listType = object : TypeToken<List<Photo>>() {}.type
        return Gson().fromJson(photosString, listType)
    }
}