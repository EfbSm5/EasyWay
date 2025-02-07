package com.efbsm5.easyway.data.repository


import android.content.Context
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.EasyPointSimplify
import com.efbsm5.easyway.data.User
import com.efbsm5.easyway.data.database.AppDataBase

class DataRepository(private val context: Context) {
    private val database = AppDataBase.getDatabase(context)

    suspend fun getAllPoints(): List<EasyPointSimplify> {
        return database.pointsDao().loadAllPoints()
    }

    suspend fun getAllDynamicPosts(): List<DynamicPost> {
        return database.dynamicPostDao().getAllDynamicPosts()
    }

    suspend fun getAllCommentsById(commentId: Int): List<Comment> {
        return database.commentDao().getCommentByCommentId(commentId)
    }

    suspend fun getUserById(userId: Int): User {
        return database.userDao().getUserById(userId) ?: User(
        )
    }
}