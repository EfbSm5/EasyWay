package com.efbsm5.easyway.database

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.data.EasyPointSimplify
import com.efbsm5.easyway.data.User
import com.efbsm5.easyway.map.MapUtil
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
fun getCount(context: Context): Int {
    val coroutineScope = rememberCoroutineScope()
    var count: Int = 0
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            count = AppDataBase.getDatabase(context).pointsDao().getCount()
        }
    }
    return count
}

@Composable
fun getCommentsCount(context: Context): Int {
    val coroutineScope = rememberCoroutineScope()
    var count: Int = 0
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            count = AppDataBase.getDatabase(context).commentDao().getCount()
        }
    }
    return count
}

@Composable
fun AddLikeForPoint(context: Context, points: EasyPoint?, pointId: Int?) {
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
fun getPoints(context: Context): List<EasyPointSimplify>? {
    val coroutineScope = rememberCoroutineScope()
    var a: List<EasyPointSimplify>? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).pointsDao().loadAllPoints()
        }
    }
    return a
}

@Composable
fun getPointContent(context: Context, pointId: Int): EasyPoint? {
    val coroutineScope = rememberCoroutineScope()
    var a: EasyPoint? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).pointsDao().getPointById(pointId)
        }
    }
    return a
}

@Composable
fun getHistory(context: Context, historyId: Int): List<EasyPoint>? {
    val coroutineScope = rememberCoroutineScope()
    var a: List<EasyPoint>? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).pointsDao().getHistory(historyId)
        }
    }
    return a
}

@Composable
fun fromMarkerToPoints(context: Context, marker: Marker): EasyPoint {
    val coroutineScope = rememberCoroutineScope()
    var points: EasyPoint? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            points = AppDataBase.getDatabase(context).pointsDao()
                .getPointByLatLng(marker.position.latitude, marker.position.longitude)
        }
    }
    return points!!
}

@Composable
fun getCommentByCommentId(context: Context, commentId: Int): List<Comment>? {
    val coroutineScope = rememberCoroutineScope()
    var a: List<Comment>? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).commentDao().getCommentByCommentId(commentId)
        }
    }
    return a
}

@Composable
fun getUserByUserId(context: Context, userId: Int): User? {
    val coroutineScope = rememberCoroutineScope()
    var a: User? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).userDao().getUserById(userId)
        }
    }
    return a
}

@Composable
fun getDynamicPostByDynamicPostId(context: Context, dynamicPostId: Int): DynamicPost? {
    val coroutineScope = rememberCoroutineScope()
    var a: DynamicPost? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).dynamicPostDao().getDynamicPostById(dynamicPostId)
        }
    }
    return a
}

@Composable
fun getAllDynamicPost(context: Context): List<DynamicPost>? {
    val coroutineScope = rememberCoroutineScope()
    var a: List<DynamicPost>? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).dynamicPostDao().getAllDynamicPosts()
        }
    }
    return a
}
