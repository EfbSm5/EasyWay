package com.efbsm5.easyway.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "dynamicposts", foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = Comment::class,
        parentColumns = arrayOf("commentid"),
        childColumns = arrayOf("commentId"),
        onDelete = ForeignKey.SET_NULL
    )]
)
data class DynamicPost(
    @PrimaryKey@SerializedName("id") val id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("date") var date: String,
    @SerializedName("like") var like: Int,
    @SerializedName("content") var content: String,
    @SerializedName("lat") var lng: Double,
    @SerializedName("lng") var lat: Double,
    @SerializedName("position") var position: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("comment_id") val commentId: Int
)

