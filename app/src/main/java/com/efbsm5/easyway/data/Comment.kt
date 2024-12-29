package com.efbsm5.easyway.data

data class Comment(
    val index: Int,
    val nickname: String,
    val content: String,
    var like: Int = 0,
    var dislike: Int = 0
)