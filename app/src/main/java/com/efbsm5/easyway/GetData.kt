package com.efbsm5.easyway

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.net.toUri
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.data.User
import com.efbsm5.easyway.data.database.AppDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl.Companion.toHttpUrl

@Composable
fun InsertEasyPointToDataBase(context: Context, points: EasyPoint) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            AppDataBase.getDatabase(context).pointsDao().insert(
                point = points
            )
        }
    }
}

@Composable
fun InsertCommentToDataBase(context: Context, comment: Comment) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            AppDataBase.getDatabase(context).commentDao().insert(
                comment = comment
            )
        }
    }
}

@Composable
fun InsertUserToDataBase(context: Context, user: User) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            AppDataBase.getDatabase(context).userDao().insert(
                user = user
            )
        }
    }
}

@Composable
fun InsertDynamicPostToDataBase(context: Context, dynamicPost: DynamicPost) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            AppDataBase.getDatabase(context).dynamicPostDao().insert(
                dynamicPost = dynamicPost
            )
        }
    }
}

@Composable
fun AddLikeForPoint(context: Context, points: EasyPoint? = null, pointId: Int? = null) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            points?.let {
                AppDataBase.getDatabase(context).pointsDao().incrementLikes(points.pointId)
            }
            pointId?.let {
                AppDataBase.getDatabase(context).commentDao().incrementLikes(pointId)
            }
        }
    }
}

@Composable
fun AddLikeForComment(context: Context, comment: Comment?, index: Int?) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            comment?.let {
                AppDataBase.getDatabase(context).commentDao().incrementLikes(comment.index)
            }
            index?.let {
                AppDataBase.getDatabase(context).commentDao().incrementLikes(index)
            }
        }
    }
}

@Composable
fun AddDisLikeForPoint(context: Context, points: EasyPoint?, pointId: Int?) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            points?.let {
                AppDataBase.getDatabase(context).pointsDao().incrementDislikes(points.pointId)
            }
            pointId?.let {
                AppDataBase.getDatabase(context).commentDao().incrementDislikes(pointId)
            }
        }
    }
}

@Composable
fun AddDisLikeForComment(context: Context, comment: Comment?, index: Int?) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            comment?.let {
                AppDataBase.getDatabase(context).commentDao().incrementDislikes(comment.index)
            }
            index?.let {
                AppDataBase.getDatabase(context).commentDao().incrementDislikes(index)
            }
        }
    }
}

@Composable
fun GetInitialData(context: Context) {
    val dataBase = AppDataBase.getDatabase(context)
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val user1 = User(
                name = "Alice",
                avatar = ("https://27142293.s21i.faiusr.com/2/ABUIABACGAAg_I_bmQYokt25kQUwwAc4gAU.jpg".toUri())
            )
            val user2 = User(
                name = "Bob",
                avatar = ("https://27142293.s21i.faiusr.com/2/ABUIABACGAAg_I_bmQYokt25kQUwwAc4gAU.jpg".toUri())
            )
            dataBase.userDao().insert(user1)
            dataBase.userDao().insert(user2)

            val comment1 = Comment(
                comment_id = 1,
                userId = user1.id,
                content = "This is a comment by Alice",
                like = 10,
                dislike = 1,
                date = "2024-12-29",
                index = 1
            )
            val comment2 = Comment(
                comment_id = 1,
                userId = user2.id,
                content = "This is a comment by Bob",
                like = 5,
                dislike = 0,
                date = "2024-12-30",
                index = 2
            )
            dataBase.commentDao().insert(comment1)
            dataBase.commentDao().insert(comment2)

            val easypoint1 = EasyPoint(
                commentId = 1,
                userId = user1.id,
                name = "EasyPoint 1",
                type = "Type 1",
                info = "Info 1",
                location = "Location 1",
                photo = ("https://27142293.s21i.faiusr.com/2/ABUIABACGAAg_I_bmQYokt25kQUwwAc4gAU.jpg".toUri()),
                refreshTime = "2024-12-29",
                likes = 100,
                dislikes = 10,
                lat = 37.7749,
                lng = -122.4194
            )
            val easypoint2 = EasyPoint(
                commentId = 1,
                userId = user2.id,
                name = "EasyPoint 2",
                type = "Type 2",
                info = "Info 2",
                location = "Location 2",
                photo = ("https://27142293.s21i.faiusr.com/2/ABUIABACGAAg_I_bmQYokt25kQUwwAc4gAU.jpg".toUri()),
                refreshTime = "2024-12-30",
                likes = 50,
                dislikes = 5,
                lat = 34.0522,
                lng = -118.2437
            )
            dataBase.pointsDao().insert(easypoint1)
            dataBase.pointsDao().insert(easypoint2)

            val dynamicpost1 = DynamicPost(
                title = "DynamicPost 1",
                date = "2024-12-29",
                like = 20,
                content = "Content 1",
                lat = 30.5155,
                lng = 114.4268,
                position = "Position 1",
                userId = user1.id,
                commentId = comment1.comment_id,
                id = 1,
                photos = emptyList()
            )
            val dynamicpost2 = DynamicPost(
                title = "DynamicPost 2",
                date = "2024-12-30",
                like = 15,
                content = "Content 2",
                lat = 31.5155,
                lng = 114.4268,
                position = "Position 2",
                userId = user2.id,
                commentId = comment2.comment_id,
                id = 2,
                photos = emptyList()
            )
            dataBase.dynamicPostDao().insert(dynamicpost1)
            dataBase.dynamicPostDao().insert(dynamicpost2)
        }
    }
}