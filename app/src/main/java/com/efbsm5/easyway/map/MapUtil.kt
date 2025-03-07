package com.efbsm5.easyway.map

import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
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

    fun initMapView(context: Context): MapView {
        return MapView(context, AMapOptions().compassEnabled(true)).apply {
            map.apply {
                mapType = if (isDarkTheme(context)) MAP_TYPE_NIGHT else MAP_TYPE_NORMAL
                isMyLocationEnabled = true
                myLocationStyle = MyLocationStyle().interval(2000)
                    .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
                showMapText(true)
                uiSettings.isMyLocationButtonEnabled = true
                uiSettings.zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_CENTER
            }
        }
    }

    fun addPoint(mapView: MapView,easyPointSimplify: EasyPointSimplify) {

    }
}