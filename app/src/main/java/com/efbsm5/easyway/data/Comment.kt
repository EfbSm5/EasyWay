package com.efbsm5.easyway.data


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
    @SerializedName("index") @PrimaryKey val index: Int,
    @SerializedName("comment_id") val comment_id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("like") var like: Int,
    @SerializedName("dislike") val dislike: Int,
    @SerializedName("date") val date: String,

    )
