package com.efbsm5.easyway.data

import androidx.room.*
import com.efbsm5.easyway.database.Converters
import com.google.gson.annotations.SerializedName
import androidx.room.ForeignKey
import java.net.URL

@Entity(
    tableName = "points", foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(Converters::class)
data class EasyPoint(
    @PrimaryKey(autoGenerate = true) @SerializedName("point_id") var pointId: Int = 0,
    @ColumnInfo(name = "name") @SerializedName("name") var name: String = "",
    @ColumnInfo(name = "type") @SerializedName("type") var type: String = "",
    @ColumnInfo(name = "info") @SerializedName("info") var info: String = "",
    @ColumnInfo(name = "location") @SerializedName("location") var location: String = "",
    @ColumnInfo(name = "photo") @SerializedName("photo") var photo: URL = URL("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG/220px-%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG"),
    @ColumnInfo(name = "time") @SerializedName("refresh_time") var refreshTime: String = "2024-12-29", // 最新更新时间
    @ColumnInfo(name = "like") @SerializedName("likes") var likes: Int = 0,
    @ColumnInfo(name = "dislike") @SerializedName("dislikes") var dislikes: Int = 0,
    @ColumnInfo(name = "lat") @SerializedName("lat") var lat: Double = 0.0,
    @ColumnInfo(name = "lng") @SerializedName("lng") var lng: Double = 0.0,
    @ColumnInfo(name = "name") @SerializedName("user_id") var userId: Int = 0,
    @ColumnInfo(name = "comments") @SerializedName("comment_id") var commentId: Int = 0,
)