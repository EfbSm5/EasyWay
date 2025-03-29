package com.efbsm5.easyway.map.overlay

import android.graphics.Bitmap
import androidx.core.graphics.scale
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint


internal object AMapServicesUtil {
    fun convertToLatLonPoint(platoon: LatLng): LatLonPoint {
        return LatLonPoint(platoon.latitude, platoon.longitude)
    }

    fun convertToLatLng(latLonPoint: LatLonPoint): LatLng {
        return LatLng(latLonPoint.latitude, latLonPoint.longitude)
    }

    fun convertArrList(shapes: List<LatLonPoint>): ArrayList<LatLng> {
        val lineShapes = ArrayList<LatLng>()
        for (point in shapes) {
            val latLngTemp = convertToLatLng(point)
            lineShapes.add(latLngTemp)
        }
        return lineShapes
    }

    fun zoomBitmap(bitmap: Bitmap?, res: Float): Bitmap? {
        if (bitmap == null) {
            return null
        }
        val width = (bitmap.width * res).toInt()
        val height = (bitmap.height * res).toInt()
        val newBmp = bitmap.scale(width, height)
        return newBmp
    }
}