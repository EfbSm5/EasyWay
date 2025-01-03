package com.efbsm5.easyway.data


import com.google.gson.annotations.SerializedName


//@Entity(
//    tableName = "comments", foreignKeys = [ForeignKey(
//        entity = User::class,
//        parentColumns = arrayOf("id"),
//        childColumns = arrayOf("userId"),
//        onDelete = ForeignKey.CASCADE
//    )]
//)
data class Comment(
    @SerializedName("index") val index: Int,
    @SerializedName("comment_id") val id: Int,
    @SerializedName("user") val userId: Int,
    @SerializedName("content") val content: String,
    @SerializedName("like") val like: Int,
    @SerializedName("dislike") val dislike: Int,
    @SerializedName("date") val date: String,

    )
