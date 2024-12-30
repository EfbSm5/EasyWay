package com.efbsm5.easyway.database

import androidx.room.TypeConverter
import com.efbsm5.easyway.data.MarkerData
import com.google.gson.Gson
import java.net.URL

class Converters {

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