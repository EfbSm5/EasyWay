package com.efbsm5.easyway.database

import androidx.room.TypeConverter
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.Comment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromComment(comment: Comment): String {
        return Gson().toJson(comment)
    }

    @TypeConverter
    fun toComment(commentString: String): Comment {
        val type = object : TypeToken<Comment>() {}.type
        return Gson().fromJson(commentString, type)
    }
    @TypeConverter
    fun fromMarker(marker: Marker): String {
        return Gson().toJson(marker)
    }

    @TypeConverter
    fun toMarker(markerString: String): Marker {
        val type = object : TypeToken<Marker>() {}.type
        return Gson().fromJson(markerString, type)
    }

}