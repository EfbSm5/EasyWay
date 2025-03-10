package com.efbsm5.easyway.map

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.core.net.toUri
import com.amap.api.maps.AMap.MAP_TYPE_NIGHT
import com.amap.api.maps.AMap.MAP_TYPE_NORMAL
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.models.assistModel.EasyPointSimplify
import com.efbsm5.easyway.ui.theme.isDarkTheme


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
        val uri = "geo:${latLng.latitude},${latLng.longitude}".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            showMsg("未找到地图应用", context)
        }
    }

    fun locationToLatlng(location: Location): LatLng {
        return LatLng(location.latitude, location.longitude)
    }

    fun Float.formatDistance(): String {
        return if (this < 500) {
            "${this.toInt()} m"  // 转为整数并以 m 为单位
        } else {
            "%.2f km".format(this / 1000)  // 转为 km 并保留两位小数
        }
    }


    fun getInitPoint(latLng: LatLng = LatLng(30.507950, 114.413514)): EasyPoint {
        return EasyPoint(
            pointId = 0,
            name = "未找到的标点",
            type = "未知类别",
            info = "无信息",
            location = "无详细地址",
            photo = "https://27142293.s21i.faiusr.com/2/ABUIABACGAAg_I_bmQYokt25kQUwwAc4gAU.jpg".toUri(),
            refreshTime = "2035.3.8",
            likes = 0,
            dislikes = 0,
            lat = latLng.latitude,
            lng = latLng.longitude,
            userId = 0,
            commentId = 0
        )
    }

    fun getInitUser(): User {
        return User(
            id = 0,
            name = "用户不存在",
            avatar = null,
        )
    }

    fun getInitPost(): DynamicPost {
        return DynamicPost(
            title = "",
            date = "",
            like = 0,
            content = "",
            lat = 30.5155,
            lng = 114.4268,
            position = "",
            userId = 1,
            commentId = 1,
            id = 1,
            photoId = 1,
            type = 1
        )
    }

    fun EasyPoint.getLatlng(): LatLng {
        return LatLng(this.lat, this.lng)
    }

}