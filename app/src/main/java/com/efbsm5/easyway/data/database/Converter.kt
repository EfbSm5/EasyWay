package com.efbsm5.easyway.data.database

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromUri(uri: Uri): String {
        return uri.toString()
    }

    @TypeConverter
    fun toUri(uriString: String): Uri {
        return Uri.parse(uriString)
    }

    @TypeConverter
    fun fromUriList(uriList: List<Uri>): String {
        return Gson().toJson(uriList)
    }

    @TypeConverter
    fun toUriList(uriListString: String): List<Uri> {
        val listType = object : TypeToken<List<Uri>>() {}.type
        return Gson().fromJson(uriListString, listType)
    }
}
