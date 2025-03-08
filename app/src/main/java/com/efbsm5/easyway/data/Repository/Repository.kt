package com.efbsm5.easyway.data.Repository


import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.models.assistModel.EasyPointSimplify
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.database.AppDataBase
import com.efbsm5.easyway.data.models.Photo
import com.efbsm5.easyway.data.network.HttpClient
import com.efbsm5.easyway.data.network.UrlForDatabase
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.map.MapUtil.getInitPoint
import com.efbsm5.easyway.map.MapUtil.getInitUser
import kotlinx.coroutines.flow.Flow

class DataRepository(private val context: Context) {
    private val database = AppDataBase.getDatabase(context)
    private val userManager = UserManager(context)
    private val httpClient = HttpClient(UrlForDatabase.BASE_URL)

    fun getAllPoints(): Flow<List<EasyPointSimplify>> {
        return database.pointsDao().loadAllPoints()
    }

    fun getAllDynamicPosts(): Flow<List<DynamicPost>> {
        return database.dynamicPostDao().getAllDynamicPosts()
    }

    fun getAllCommentsById(commentId: Int): Flow<List<Comment>> {
        return database.commentDao().getCommentByCommentId(commentId)
    }

    fun getUserById(userId: Int): User {
        return database.userDao().getUserById(userId) ?: getInitUser()
    }

    fun addLike(commentId: Int) {
        database.commentDao().increaseLikes(commentId)
    }

    fun decreaseLike(commentId: Int) {
        database.commentDao().decreaseLikes(commentId)
    }

    fun addDisLike(commentId: Int) {
        database.commentDao().increaseDislikes(commentId)
    }

    fun uploadPost(dynamicPost: DynamicPost, photos: List<Uri>) {
        val id = database.dynamicPostDao().getCount() + 1
        val date = MapUtil.getCurrentFormattedTime()
        val commentId = database.commentDao().getMaxCommentId() + 1
        val photoId = database.photoDao().getMaxCommentId() + 1
        photos.forEach { uri ->
            httpClient.uploadImage(
                context, uri,
                callback = {
                    if (it != null) {
                        database.photoDao().insert(
                            Photo(
                                uri = it.toUri(),
                                photoId = photoId,
                                id = database.photoDao().getCount() + 1
                            )
                        )
                    }
                },
            )
        }
        val post = DynamicPost(
            id = id,
            title = dynamicPost.title,
            date = date,
            like = 0,
            content = dynamicPost.content,
            lng = dynamicPost.lng,
            lat = dynamicPost.lat,
            position = dynamicPost.position,
            userId = userManager.userId,
            commentId = commentId,
            photoId = photoId
        )


        database.dynamicPostDao().insert(post)
    }

    fun getPointFromLatlng(latLng: LatLng): EasyPoint {
        return database.pointsDao().getPointByLatLng(latLng.latitude, latLng.longitude)
            ?: getInitPoint(latLng)
    }

    fun uploadComment(comment: Comment) {
        database.commentDao().insert(comment)
    }

    fun uploadPoint(easyPoint: EasyPoint) {
        EasyPoint(
            pointId = database.pointsDao().getCount() + 1,
            name = easyPoint.name,
            type = easyPoint.type,
            info = easyPoint.info,
            location = easyPoint.location,
            photo = easyPoint.photo,
            refreshTime = MapUtil.getCurrentFormattedTime(),
            likes = 0,
            dislikes = 0,
            lat = easyPoint.lat,
            lng = easyPoint.lng,
            userId = userManager.userId,
            commentId = database.commentDao().getMaxCommentId() + 1
        ).let {
            database.pointsDao().insert(it)
        }
    }

    fun getCommentCount(commentId: Int): Flow<Int> {
        return database.commentDao().getCountById(commentId)
    }

    fun getAllPhotosById(photoId: Int): List<Photo> {
        return database.photoDao().getPhotoById(id = photoId)
    }

    fun getPostByUserId(userId: Int): Flow<List<DynamicPost>> {
        return database.dynamicPostDao().getAllDynamicPostsByUserId(userId)
    }

    fun getPointByUserId(userId: Int): Flow<List<EasyPoint>> {
        return database.pointsDao().getPointByUserId(userId)
    }

    fun getCommentByUserId(userId: Int): Flow<List<Comment>> {
        return database.commentDao().getCommentByUserId(userId)
    }

    fun getCommentCount(): Int {
        return database.commentDao().getCount()
    }
}