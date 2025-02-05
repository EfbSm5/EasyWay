package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.data.database.AppDataBase
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _comments = MutableStateFlow<List<Comment>?>(null)
    private val _point = MutableStateFlow(EasyPoint())
    val comments: StateFlow<List<Comment>?> = _comments
    val point: StateFlow<EasyPoint> = _point


    fun fetchCommentsById(commentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _comments.value = repository.getAllCommentsById(commentId)
        }
    }

    fun insertComment(comment: Comment, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context)
            val _comment = comment.copy(
                date = MapUtil.getCurrentFormattedTime(),
                index = database.commentDao().getCount() + 1,
            )
            database.commentDao().insert(_comment)
        }
    }

    fun getCommentFromMarker(marker: Marker, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context)
            val fetchedPoint = database.pointsDao()
                .getPointByLatLng(marker.position.latitude, marker.position.longitude)
            fetchedPoint?.let {
                val fetchedComments = database.commentDao().getCommentByCommentId(it.commentId)
                _point.value = fetchedPoint
                _comments.value = fetchedComments
            }
        }
    }

}