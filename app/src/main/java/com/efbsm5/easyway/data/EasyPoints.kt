package com.efbsm5.easyway.data

import androidx.room.*
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.database.CommentConverter
import com.efbsm5.easyway.database.Converters

@Entity(tableName = "points")
@TypeConverters(CommentConverter::class, Converters::class)
data class EasyPoints(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    @ColumnInfo(name = "pointId") val pointId: Int = 1,
    @ColumnInfo(name = "type") val type: String = "不详",
    @ColumnInfo(name = "info") val introduce: String = "不详",
    @ColumnInfo(name = "name") val name: String = "不详",
    @TypeConverters(Converters::class) @ColumnInfo(name = "location") val position: LatLng? = null,
    @ColumnInfo(name = "photo") val photos: ByteArray? = null,
    @TypeConverters(CommentConverter::class) @ColumnInfo(name = "comment") val comments: Comment? = null,
    @ColumnInfo(name = "time") val refreshTime: String? = null,
    @ColumnInfo(name = "like") val likes: Int = 0,
    @ColumnInfo(name = "dislike") val dislikes: Int = 0,
)