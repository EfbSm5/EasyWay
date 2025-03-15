package com.efbsm5.easyway.map

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.amap.api.maps.model.LatLng

class LocationSaver(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MapPreferences", Context.MODE_PRIVATE)

    var location: LatLng
        get() = with(sharedPreferences) {
            LatLng(
                getFloat("last_lat", Float.NaN).toDouble(),
                getFloat("last_lng", Float.NaN).toDouble()
            )
        }
        set(value) {
            sharedPreferences.edit {
                putFloat("last_lat", value.latitude.toFloat())
                putFloat("last_lng", value.longitude.toFloat())
            }
        }
    var cityCode: String
        get() = sharedPreferences.getString("citycode", "027") ?: "027"
        set(value) {
            sharedPreferences.edit {
                putString("citycode", value)
            }
        }
    var locationDetail: String
        get() = sharedPreferences.getString("detail", "武汉市") ?: "定位失败"
        set(value) {
            sharedPreferences.edit {
                putString("detail", value)
            }
        }
}
