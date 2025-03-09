package com.efbsm5.easyway.viewmodel.componentsViewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.model.Poi
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.Repository.DataRepository
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.assistModel.CommentAndUser
import com.efbsm5.easyway.map.MapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class CommentAndHistoryCardViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _state = MutableStateFlow<CommentCardScreen>(CommentCardScreen.Comment)
    private val _point = MutableStateFlow(MapUtil.getInitPoint())
    private var _pointComments = MutableStateFlow<List<CommentAndUser>>(emptyList())
    private val userManager = UserManager(context)
    val point: StateFlow<EasyPoint> = _point
    val state: StateFlow<CommentCardScreen> = _state
    val pointComments: StateFlow<List<CommentAndUser>> = _pointComments

    fun changeState(commentCardScreen: CommentCardScreen) {
        _state.value = commentCardScreen
    }
    fun publish(string: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val comment = Comment(
                index = repository.getCommentCount(),
                commentId = _point.value.commentId,
                userId = userManager.userId,
                content = string,
                like = 0,
                dislike = 0,
                date = MapUtil.getCurrentFormattedTime()
            )
            repository.uploadComment(
                comment = comment
            )
            _pointComments.value = _pointComments.value.plus(
                CommentAndUser(
                    repository.getUserById(userManager.userId), comment
                )
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

    fun addEasyPoint(easyPoint: EasyPoint) {
        _point.value = easyPoint
    }

    fun likePost(boolean: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (boolean) {
                _point.value = _point.value.copy(likes = _point.value.likes + 1)
                repository.addLikeForPoint(point.value.pointId)
            } else {
                _point.value = _point.value.copy(likes = _point.value.likes - 1)
                repository.decreaseLikeForPoint(point.value.pointId)
            }
        }
    }

    fun dislikePost(boolean: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (boolean) {
                _point.value = _point.value.copy(dislikes = _point.value.dislikes + 1)
                repository.addDisLikeForPoint(point.value.pointId)
            } else {
                _point.value = _point.value.copy(dislikes = _point.value.dislikes - 1)
                repository.decreaseDisLikeForPoint(point.value.pointId)
            }
        }
    }

    fun likeComment(commentIndex: Int, boolean: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (boolean) {
                _pointComments.value.find { commentAndUser ->
                    commentAndUser.comment.index == commentIndex
                }!!.comment.like + 1
                repository.addLikeForComment(commentIndex)
            } else {
                _pointComments.value.find { commentAndUser ->
                    commentAndUser.comment.index == commentIndex
                }!!.comment.like - 1
                repository.decreaseLikeForComment(commentIndex)
            }
        }
    }

    fun dislikeComment(commentIndex: Int, boolean: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            if (boolean) {
                _pointComments.value.find { commentAndUser ->
                    commentAndUser.comment.index == commentIndex
                }!!.comment.dislike + 1
                repository.addDisLikeForComment(commentIndex)
            } else {
                _pointComments.value.find { commentAndUser ->
                    commentAndUser.comment.index == commentIndex
                }!!.comment.dislike - 1
                repository.decreaseDisLikeForComment(commentIndex)
            }
        }
    }
}

sealed interface CommentCardScreen {
    data object Comment : CommentCardScreen
    data object History : CommentCardScreen
}