package com.efbsm5.easyway.database

import androidx.room.TypeConverter
import com.efbsm5.easyway.data.Comment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CommentConverter {
    @TypeConverter
    fun fromComment(comment: Comment): String {
        return Gson().toJson(comment)
    }

    @TypeConverter
    fun toComment(commentString: String): Comment {
        val type = object : TypeToken<Comment>() {}.type
        return Gson().fromJson(commentString, type)
    }
}