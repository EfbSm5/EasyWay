package com.efbsm5.easyway.data

import android.net.Uri
import com.amap.api.maps.model.LatLng

data class DynamicPost(
    var title: String = "",
    var date: String = "",
    var host: String = "",
    var like: Int = 0,
    var content: String = "",
    var comment: ArrayList<Comment> = ArrayList(),
    var location: LatLng? = null,
    var position: String = "",
    var photos: ArrayList<Uri> = ArrayList(),
    val user: User
)
