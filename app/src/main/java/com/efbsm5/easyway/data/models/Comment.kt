package com.efbsm5.easyway.data.models


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(
    tableName = "comments", foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Comment(
    @SerializedName("index") @PrimaryKey val index: Int = 0,
    @SerializedName("comment_id") val comment_id: Int = 0,
    @SerializedName("user_id") val userId: Int = 0,
    @SerializedName("content") var content: String = "",
    @SerializedName("like") var like: Int = 0,
    @SerializedName("dislike") val dislike: Int = 0,
    @SerializedName("date") val date: String = "",
)
