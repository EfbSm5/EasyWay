package com.efbsm5.easyway.map

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.widget.Toast
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.efbsm5.easyway.data.models.Comment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object MapUtil {
    fun convertToLatLonPoint(latLng: LatLng): LatLonPoint {
        return LatLonPoint(latLng.latitude, latLng.longitude)
    }

    fun convertToLatLng(latLonPoint: LatLonPoint): LatLng {
        return LatLng(latLonPoint.latitude, latLonPoint.longitude)
    }

    fun showMsg(text: String, context: Context) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun fromComment(comment: ArrayList<Comment>): String {
        return Gson().toJson(comment)
    }

    fun toComment(commentString: String): ArrayList<Comment> {
        val type = object : TypeToken<Comment>() {}.type
        return Gson().fromJson(commentString, type)
    }

    fun getImageUrl(context: Context, imageName: String): String {
        val resId = context.resources.getIdentifier(imageName, "drawable", context.packageName)
        return "android.resource://${context.packageName}/$resId"
    }

    fun getCurrentFormattedTime(): String {
        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return currentTime.format(formatter)
    }

    fun calculateDistance(point1: LatLng, point2: LatLng): Float {
        val location1 = Location("").apply {
            latitude = point1.latitude
            longitude = point1.longitude
        }
        val location2 = Location("").apply {
            latitude = point2.latitude
            longitude = point2.longitude
        }
        return location1.distanceTo(location2)
    }

    fun onNavigate(context: Context, latLng: LatLng) {
        val uri = Uri.parse("geo:${latLng.latitude},${latLng.longitude}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "未找到地图应用", Toast.LENGTH_SHORT).show()
        }
    }

    fun Float.formatDistance(): String {
        return if (this < 500) {
            "${this.toInt()} m"  // 转为整数并以 m 为单位
        } else {
            "%.2f km".format(this / 1000)  // 转为 km 并保留两位小数
        }
    }

}