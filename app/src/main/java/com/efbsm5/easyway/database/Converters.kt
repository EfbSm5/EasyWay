package com.efbsm5.easyway.database

import androidx.room.TypeConverter
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.MarkerData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL

class Converters {
    @TypeConverter
    fun fromCommentList(comments: ArrayList<Comment>): String? {
        return Gson().toJson(comments)
    }

    @TypeConverter
    fun toCommentList(commentsString: String?): ArrayList<Comment>? {
        return commentsString?.let {
            val listType = object : TypeToken<ArrayList<Comment>>() {}.type
            Gson().fromJson(it, listType)
        }
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
    fun fromMarkerData(markerData: MarkerData): String {
        return Gson().toJson(markerData)
    }

    @TypeConverter
    fun toMarkerData(markerDataString: String): MarkerData? {
        return Gson().fromJson(markerDataString, MarkerData::class.java)
    }
}