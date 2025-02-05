package com.efbsm5.easyway.data.database

import android.net.Uri
import androidx.room.TypeConverter
import com.efbsm5.easyway.data.Photo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL

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

    @TypeConverter
    fun fromUrl(url: URL): String {
        return url.toString()
    }

    @TypeConverter
    fun toUrl(urlString: String): URL {
        return URL(urlString)
    }

    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(uriString: String): Uri {
        return Uri.parse(uriString)
    }
}
