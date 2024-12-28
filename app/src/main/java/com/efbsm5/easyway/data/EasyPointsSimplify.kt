package com.efbsm5.easyway.data

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.database.Converters

data class EasyPointsSimplify(
    @PrimaryKey(autoGenerate = true) val id: Int = 1,
    @ColumnInfo(name = "name") val name: String = "不详",
    @TypeConverters(Converters::class) @ColumnInfo(name = "location") val position: LatLng? = null,
)
