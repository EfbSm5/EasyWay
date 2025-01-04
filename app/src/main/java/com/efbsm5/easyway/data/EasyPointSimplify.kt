package com.efbsm5.easyway.data

import com.google.gson.annotations.SerializedName

data class EasyPointSimplify(
    @SerializedName("point_id") val pointId: Int = 1,
    @SerializedName("name") val name: String = "",
    @SerializedName("lat") val lat: Double = 0.0,
    @SerializedName("lng") val lng: Double= 0.0,
)
