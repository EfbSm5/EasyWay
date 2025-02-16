package com.efbsm5.easyway.data.ViewModelRepository


import android.content.Context
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.EasyPoint
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

    suspend fun addLike(commentId: Int) {
        database.commentDao().incrementLikes(commentId)
    }

    suspend fun uploadPost(dynamicPost: DynamicPost) {
        database.dynamicPostDao().insert(dynamicPost)
    }

    suspend fun getPointFromMarker(marker: Marker): EasyPoint? {
        return database.pointsDao()
            .getPointByLatLng(marker.position.latitude, marker.position.longitude)
    }

    suspend fun uploadComment(comment: Comment) {
        database.commentDao().insert(comment)
    }

}