package com.efbsm5.easyway.data.models

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "photos")
data class Photo(
    @PrimaryKey @SerializedName("id") val id: Int,
    @SerializedName("url") var uri: Uri,
    @SerializedName("photo_id") val photoId: Int
)