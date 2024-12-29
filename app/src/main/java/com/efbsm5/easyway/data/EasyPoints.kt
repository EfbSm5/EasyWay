package com.efbsm5.easyway.data

import androidx.room.*
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.database.Converters
import java.net.URL

@Entity(tableName = "points")
@TypeConverters(Converters::class)
data class EasyPoints(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    @ColumnInfo(name = "pointId") val pointId: Int = 1,
    @ColumnInfo(name = "type") val type: String = "不详",
    @ColumnInfo(name = "info") val introduce: String = "不详",
    @ColumnInfo(name = "name") val name: String = "不详",
    @ColumnInfo(name = "photo") val photos: URL? = null,
    @ColumnInfo(name = "comment") val comments: String? = null,
    @ColumnInfo(name = "time") val refreshTime: String? = null,
    @ColumnInfo(name = "like") val likes: Int = 0,
    @ColumnInfo(name = "dislike") val dislikes: Int = 0,
    @ColumnInfo(name = "marker") val marker: MarkerData? = null
)