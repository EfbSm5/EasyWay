package com.efbsm5.easyway.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.database.Converters

@Entity(tableName = "points")
data class EasyPoints(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,

    @ColumnInfo(name = "type") val type: String = "不详",

    @ColumnInfo(name = "info") val name: String = "不详",

    @TypeConverters(Converters::class) @ColumnInfo(name = "location") val position: LatLng? = null,

    @TypeConverters(Converters::class) @ColumnInfo(name = "photo") val photos: Uri? = null,

    @ColumnInfo(name = "like") val likes: Int = 0,

    @ColumnInfo(name = "dislike") val dislikes: Int = 0,
)

