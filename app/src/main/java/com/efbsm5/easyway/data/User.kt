package com.efbsm5.easyway.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) @SerializedName("id") val id: Int = 0,
    @ColumnInfo(name = "name") @SerializedName("name") val name: String = "用户不存在",
    @ColumnInfo(name = "avatar") @SerializedName("avatar") val avatar: Uri? = null
)