package com.efbsm5.easyway

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.data.User
import com.efbsm5.easyway.database.AppDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
