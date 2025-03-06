package com.efbsm5.easyway.viewmodel.componentsViewmodel

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.Poi
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import com.efbsm5.easyway.data.models.assistModel.CommentAndUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "CommentAndHistoryCardVi"

class CommentAndHistoryCardViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _state = MutableStateFlow<CommentCardScreen>(CommentCardScreen.Comment)
    private val _point = MutableStateFlow<EasyPoint?>(null)
    private var _showComment = MutableStateFlow(false)
    private var _pointComments = MutableStateFlow<List<CommentAndUser>?>(null)
    private var _newComment = MutableStateFlow("")
    val point: StateFlow<EasyPoint?> = _point
    val state: StateFlow<CommentCardScreen> = _state
    val showComment: StateFlow<Boolean> = _showComment
    val pointComments: StateFlow<List<CommentAndUser>?> = _pointComments
    val newComment: StateFlow<String> = _newComment

    fun getPoint(latLng: LatLng) {
        viewModelScope.launch(Dispatchers.IO) {
            _point.value = repository.getPointFromLatlng(latLng)
            if (_point.value == null) {
                Log.e(TAG, "getPoint:     null point")
            } else repository.getAllCommentsById(_point.value!!.commentId).collect { comments ->
                val commentsAndUser = emptyList<CommentAndUser>().toMutableList()
                comments.forEach {
                    commentsAndUser.add(
                        CommentAndUser(
                            repository.getUserById(it.userId), it
                        )
                    )
                }
                _pointComments.value = commentsAndUser

            }
        }
    }

    fun changeState(commentCardScreen: CommentCardScreen) {
        _state.value = commentCardScreen
    }

    fun showComment(boolean: Boolean) {
        _showComment.value = boolean
    }

    fun editComment(string: String) {
        _newComment.value = string
    }

    fun publish() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadComment(
                _newComment.value, commentId = _point.value!!.commentId
            )
        }
    }

    fun addPoiItem(poiItemV2: PoiItemV2) {
        _point.value = EasyPoint(
            pointId = 0,
            name = poiItemV2.title,
            type = "一般点",
            info = poiItemV2.snippet,
            location = "无详细描述",
            photo = poiItemV2.photos.first().url.toUri(),
            refreshTime = "未知",
            likes = 0,
            dislikes = 0,
            lat = poiItemV2.latLonPoint.latitude,
            lng = poiItemV2.latLonPoint.longitude,
            userId = 0,
            commentId = 0
        )
    }

    fun addPoi(poi: Poi) {
        _point.value = EasyPoint(
            pointId = 0,
            name = poi.name,
            type = "一般点",
            info = "无详细描述",
            location = "无详细描述",
            photo = null,
            refreshTime = "未知",
            likes = 0,
            dislikes = 0,
            lat = poi.coordinate.latitude,
            lng = poi.coordinate.longitude,
            userId = 0,
            commentId = 0
        )
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            val commentsAndUser = emptyList<CommentAndUser>().toMutableList()
            repository.getAllCommentsById(_point.value!!.commentId).collect { comments ->
                comments.forEach {
                    commentsAndUser.add(
                        CommentAndUser(
                            repository.getUserById(it.userId), it
                        )
                    )
                }
            }
        }
    }
}

sealed interface CommentCardScreen {
    data object Comment : CommentCardScreen
    data object History : CommentCardScreen
}