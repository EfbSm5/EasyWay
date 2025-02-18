package com.efbsm5.easyway.data.models

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.*
import com.google.gson.annotations.SerializedName
import androidx.room.ForeignKey

@Entity(
    tableName = "points", foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class EasyPoint(
    @PrimaryKey(autoGenerate = true) @SerializedName("point_id") var pointId: Int,
    @ColumnInfo(name = "name") @SerializedName("name") var name: String,
    @ColumnInfo(name = "type") @SerializedName("type") var type: String,
    @ColumnInfo(name = "info") @SerializedName("info") var info: String,
    @ColumnInfo(name = "location") @SerializedName("location") var location: String,
    @ColumnInfo(name = "photo") @SerializedName("photo") var photo: Uri = ("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG/220px-%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG".toUri()),
    @ColumnInfo(name = "time") @SerializedName("refresh_time") var refreshTime: String,
    @ColumnInfo(name = "like") @SerializedName("likes") var likes: Int,
    @ColumnInfo(name = "dislike") @SerializedName("dislikes") var dislikes: Int,
    @ColumnInfo(name = "lat") @SerializedName("lat") var lat: Double,
    @ColumnInfo(name = "lng") @SerializedName("lng") var lng: Double,
    @ColumnInfo(name = "user_id") @SerializedName("user_id") var userId: Int,
    @ColumnInfo(name = "comments") @SerializedName("comment_id") var commentId: Int,
)