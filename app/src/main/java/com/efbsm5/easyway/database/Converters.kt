package com.efbsm5.easyway.database

import android.net.Uri
import androidx.room.TypeConverter
import com.amap.api.maps.model.LatLng

class Converters {
    @TypeConverter
    fun fromLatLng(latLng: LatLng?): String? {
        return latLng?.let { "${it.latitude},${it.longitude}" }
    }

    @TypeConverter
    fun toLatLng(value: String?): LatLng? {
        return value?.split(",")?.let {
            LatLng(it[0].toDouble(), it[1].toDouble())
        }
    }

    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(value: String?): Uri? {
        return value?.let { Uri.parse(it) }
    }
}