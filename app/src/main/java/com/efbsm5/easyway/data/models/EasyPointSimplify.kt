package com.efbsm5.easyway.data.models

import com.google.gson.annotations.SerializedName

data class EasyPointSimplify(
    @SerializedName("point_id") val pointId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
)
