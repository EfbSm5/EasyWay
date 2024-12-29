package com.efbsm5.easyway.ultis

import android.content.Context
import android.widget.Toast
import androidx.room.TypeConverter
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.LatLonPoint
import com.efbsm5.easyway.data.Comment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
}