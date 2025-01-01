package com.efbsm5.easyway.data

import androidx.room.*
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.database.Converters
import java.net.URL

@Entity(tableName = "points")
@TypeConverters(Converters::class)
data class EasyPoint(
    @PrimaryKey(autoGenerate = true) var id: Int = 1,
    @ColumnInfo(name = "pointId") var pointId: Int = 1,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "type") var type: String = "",
    @ColumnInfo(name = "info") var introduce: String = "",
    @ColumnInfo(name = "location") var location: String = "",
    @ColumnInfo(name = "photo") var photos: URL = URL(
        "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG/220px-%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG"
    ),
    @ColumnInfo(name = "comment") var comments: String? = null,//评论
    @ColumnInfo(name = "time") var refreshTime: String = "2024-12-29",//最新更新时间
    @ColumnInfo(name = "like") var likes: Int = 0,
    @ColumnInfo(name = "dislike") var dislikes: Int = 0,
    @ColumnInfo(name = "latLng") var latLng: LatLng = LatLng(0.0, 0.0),
)