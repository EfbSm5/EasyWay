package com.efbsm5.easyway.database

import androidx.room.TypeConverter
import com.amap.api.maps.model.LatLng
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
    fun fromLatLng(latLng: LatLng): String {
        return Gson().toJson(latLng)
    }

    @TypeConverter
    fun toLatLng(latLngString: String): LatLng {
        return Gson().fromJson(latLngString, LatLng::class.java)
    }
}