package com.efbsm5.easyway.data

import com.google.gson.annotations.SerializedName

data class DynamicPost(
    @SerializedName("id") val id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("date") var date: String,
    @SerializedName("like") var like: Int,
    @SerializedName("content") var content: String,
    @SerializedName("lat") var lng: Double,
    @SerializedName("lng") var lat: Double,
    @SerializedName("position") var position: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("comment_id") val commentId: Int
)

