package com.efbsm5.easyway.data

data class Comment(
    val index: Int,
    val content: String,
    var like: Int = 0,
    var dislike: Int = 0,
    val date: String = "",
    val user: User
)