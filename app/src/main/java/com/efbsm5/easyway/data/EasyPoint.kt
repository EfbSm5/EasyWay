package com.efbsm5.easyway.data

import androidx.room.*
import com.efbsm5.easyway.database.Converters
import java.net.URL

@Entity(tableName = "points")
@TypeConverters(Converters::class)
data class EasyPoint(
    @PrimaryKey(autoGenerate = true) var id: Int = 1,
    @ColumnInfo(name = "pointId") var pointId: Int = 1,
    @ColumnInfo(name = "type") var type: String = "不详",
    @ColumnInfo(name = "info") var introduce: String = "不详",
    @ColumnInfo(name = "location") var location: String = "不详",
    @ColumnInfo(name = "photo") var photos: URL? = null,//图片
    @ColumnInfo(name = "comment") var comments: String? = null,//评论
    @ColumnInfo(name = "time") var refreshTime: String? = null,//最新更新时间
    @ColumnInfo(name = "like") var likes: Int = 0,
    @ColumnInfo(name = "dislike") var dislikes: Int = 0,
    @ColumnInfo(name = "marker") var marker: MarkerData? = null//点位信息
)