package com.efbsm5.easyway.network

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import com.efbsm5.easyway.data.EasyPoints
import com.efbsm5.easyway.database.AppDataBase
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
fun GetDataFromDataBase(context: Context): List<EasyPoints>? {
    val coroutineScope = rememberCoroutineScope()
    var a: List<EasyPoints>? = null
    LaunchedEffect(Unit) {
        coroutineScope.launch(Dispatchers.IO) {
            a = AppDataBase.getDatabase(context).userDao().loadAllPoints()
        }
    }
    return a
}