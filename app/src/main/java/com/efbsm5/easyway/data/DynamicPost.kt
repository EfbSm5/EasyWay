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
    val user: User = User(
        name = "developer",
        avatar = Uri.parse("https://bkimg.cdn.bcebos.com/pic/2f738bd4b31c8701a18be16bb327892f0708293851bf?x-bce-process=image/resize,m_lfit,w_525,h_700,limit_0/quality,Q_90\n")
    )
)

