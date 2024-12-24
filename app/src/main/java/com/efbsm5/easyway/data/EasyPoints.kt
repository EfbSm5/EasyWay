package com.efbsm5.easyway.data

import androidx.databinding.adapters.Converters
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.amap.api.maps.model.LatLng

@Entity(tableName = "points")
data class EasyPoints {
    @PrimaryKey(autoGenerate = true)
    val id: Int = 1

    @ColumnInfo(name = "name")
    val name: String = "默认昵称"

    @ColumnInfo(name = "sex")
    val sex: String = "不详"


    @ColumnInfo(name = "useEmotion")
    val useEmotion: Float = 0.5f

    @TypeConverters(Converters::class)
    @ColumnInfo(name = "photoFile")
    val position: LatLng? = null
}

