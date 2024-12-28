package com.efbsm5.easyway.data

data class Comment(
    val nickname: String,
    val content: String,
    val likes: Int = 0,
    val dislikes: Int = 0
)