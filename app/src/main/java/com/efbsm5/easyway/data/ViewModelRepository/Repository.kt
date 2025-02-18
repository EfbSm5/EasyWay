package com.efbsm5.easyway.data.ViewModelRepository


import android.content.Context
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.models.EasyPointSimplify
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.database.AppDataBase
import com.efbsm5.easyway.map.MapUtil

class DataRepository(private val context: Context) {
    private val database = AppDataBase.getDatabase(context)
    private val userManager = UserManager(context)

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
            id = 0,
            name = "用户不存在",
            avatar = null,
        )
    }

    suspend fun addLike(commentId: Int) {
        database.commentDao().increaseLikes(commentId)
    }

    suspend fun addDisLike(commentId: Int) {
        database.commentDao().increaseDislikes(commentId)
    }

    suspend fun uploadPost(dynamicPost: DynamicPost) {
        val id = database.dynamicPostDao().getCount() + 1
        val date = MapUtil.getCurrentFormattedTime()
        val commentId = database.commentDao().getMaxCommentId() + 1
        val _post = DynamicPost(
            id = id,
            title = dynamicPost.title,
            date = date,
            like = 0,
            content = dynamicPost.content,
            lng = 0.0,
            lat = 0.0,
            position = dynamicPost.position,
            userId = userManager.userId,
            commentId = commentId,
            photos = emptyList()
        )
        database.dynamicPostDao().insert(dynamicPost)
    }

    suspend fun getPointFromMarker(marker: Marker): EasyPoint? {
        return database.pointsDao()
            .getPointByLatLng(marker.position.latitude, marker.position.longitude)
    }

    suspend fun uploadComment(commentContent: String, commentId: Int) {
        val index = database.commentDao().getCount() + 1
        val time = MapUtil.getCurrentFormattedTime()
        val comment = Comment(
            index = index,
            comment_id = commentId,
            userId = userManager.userId,
            content = commentContent,
            like = 0,
            dislike = 0,
            date = time
        )
        database.commentDao().insert(comment)
    }

    suspend fun uploadPoint(point: EasyPoint) {
        val _point = point
        val pointId = database.pointsDao().getCount() + 1
        val refreshTime = MapUtil.getCurrentFormattedTime()
        val userId = 1
        val commentId = database.commentDao().getMaxCommentId() + 1
        _point.commentId = commentId
        _point.userId = userId
        _point.refreshTime = refreshTime
        _point.pointId = pointId
        database.pointsDao().insert(point)
    }

}