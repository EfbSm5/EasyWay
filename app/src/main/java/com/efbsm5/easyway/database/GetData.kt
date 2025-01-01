package com.efbsm5.easyway.database

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.data.EasyPointSimplify
import com.efbsm5.easyway.map.MapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun InsertDataToDataBase(context: Context, points: EasyPoint) {
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
fun getCount(context: Context): Int {
    val coroutineScope = rememberCoroutineScope()
    var count: Int = 1
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            count = AppDataBase.getDatabase(context).userDao().getCount()
        }
    }
    return count
}

@Composable
fun AddLike(context: Context, points: EasyPoint) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            AppDataBase.getDatabase(context).userDao().incrementLikes(points.id)
        }
    }
}

@Composable
fun AddDisLike(context: Context, points: EasyPoint) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            AppDataBase.getDatabase(context).userDao().incrementDislikes(points.id)
        }
    }
}

@Composable
fun getPoints(context: Context): List<EasyPointSimplify>? {
    val coroutineScope = rememberCoroutineScope()
    var a: List<EasyPointSimplify>? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).userDao().loadAllPoints()
        }
    }
    return a
}

@Composable
fun getPointContent(context: Context, id: Int): EasyPoint? {
    val coroutineScope = rememberCoroutineScope()
    var a: EasyPoint? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).userDao().getPointById(id)
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
            a = AppDataBase.getDatabase(context).userDao().getHistory(historyId)
        }
    }
    return a
}

@Composable
fun IncrementCommentLikes(context: Context, id: Int, index: Int) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context).userDao()
            val comment = database.getCommentById(id)
            if (comment != null) {
                val temp = MapUtil.toComment(comment)
                temp[index].like += 1
                val json = MapUtil.fromComment(temp)
                database.updateComment(id, json)
            }
        }
    }
}

@Composable
fun IncrementCommentDislikes(context: Context, id: Int, index: Int) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context).userDao()
            val comment = database.getCommentById(id)
            if (comment != null) {
                val temp = MapUtil.toComment(comment)
                temp[index].dislike += 1
                val json = MapUtil.fromComment(temp)
                database.updateComment(id, json)
            }
        }
    }
}

@Composable
fun fromMarkerToPoints(context: Context, marker: Marker): EasyPoint {
    val coroutineScope = rememberCoroutineScope()
    var points = EasyPoint()
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context).userDao()
            val latLng = marker.position
            points = database.markerToPoints(latLng)
        }
    }
    return points
}

