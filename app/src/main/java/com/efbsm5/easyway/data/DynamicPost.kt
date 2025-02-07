package com.efbsm5.easyway.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "dynamicposts", foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class DynamicPost(
    @PrimaryKey @SerializedName("id") var id: Int = 0,
    @SerializedName("title") var title: String = "",
    @SerializedName("date") var date: String = "",
    @SerializedName("like") var like: Int = 0,
    @SerializedName("content") var content: String = "",
    @SerializedName("lat") var lat: Double = 0.0,
    @SerializedName("lng") var lng: Double = 0.0,
    @SerializedName("position") var position: String = "",
    @SerializedName("user_id") var userId: Int = 0,
    @SerializedName("comment_id") var commentId: Int = 0,
    @SerializedName("photos") var photos: List<Photo> = emptyList()
)

@Entity(
    tableName = "photos", foreignKeys = [ForeignKey(
        entity = DynamicPost::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("dynamicpostId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Photo(
    @PrimaryKey @SerializedName("id") val id: Int,
    @SerializedName("url") var url: String,
    @SerializedName("dynamicpost_id") val dynamicpostId: Int
)