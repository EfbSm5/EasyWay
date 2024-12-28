package com.efbsm5.easyway.database

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.EasyPoints
import com.efbsm5.easyway.data.EasyPointsSimplify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun InsertDataToDataBase(context: Context, points: EasyPoints) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            AppDataBase.getDatabase(context).userDao().insert(
                point = points
            )
        }
    }
}

@Composable
fun AddLike(context: Context, points: EasyPoints) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            AppDataBase.getDatabase(context).userDao().incrementLikes(points.id)
        }
    }
}

@Composable
fun AddDisLike(context: Context, points: EasyPoints) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            AppDataBase.getDatabase(context).userDao().incrementDislikes(points.id)
        }
    }
}

@Composable
fun getPoints(context: Context): List<EasyPointsSimplify>? {
    val coroutineScope = rememberCoroutineScope()
    var a: List<EasyPointsSimplify>? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).userDao().loadAllPoints()
        }
    }
    return a
}

@Composable
fun getPointContent(context: Context, id: Int): EasyPoints? {
    val coroutineScope = rememberCoroutineScope()
    var a: EasyPoints? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).userDao().getPointById(id)
        }
    }
    return a
}

@Composable
fun getHistory(context: Context, historyId: Int): List<EasyPoints>? {
    val coroutineScope = rememberCoroutineScope()
    var a: List<EasyPoints>? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).userDao().getHistory(historyId)
        }
    }
    return a
}

@Composable
fun IncrementCommentLikes(context: Context, id: Int) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context).userDao()
            val comment = database.getCommentById(id)
            if (comment != null) {
                val updatedComment = comment.copy(likes = comment.likes + 1)
                database.updateComment(id, updatedComment)
            }
        }
    }
}

@Composable
fun IncrementCommentDislikes(context: Context, id: Int) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context).userDao()
            val comment = database.getCommentById(id)
            if (comment != null) {
                val updatedComment = comment.copy(dislikes = comment.dislikes + 1)
                database.updateComment(id, updatedComment)
            }
        }
    }
}

@Composable
fun fromMarkerToPoints(context: Context, marker: Marker): EasyPoints {
    val coroutineScope = rememberCoroutineScope()
    var points = EasyPoints()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context).userDao()
            points = database.markerToPoints(marker)
        }
    }
    return points
}

