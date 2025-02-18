package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentAndHistoryCardViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _state = MutableStateFlow<CommentCardScreen>(CommentCardScreen.Comment)
    private var _point = MutableStateFlow<EasyPoint?>(null)
    private var _showComment = MutableStateFlow(false)
    private var _pointComments = MutableStateFlow<List<Comment>?>(null)
    private var _newComment = MutableStateFlow<Comment?>(null)
    val point: StateFlow<EasyPoint?> = _point
    val state: StateFlow<CommentCardScreen> = _state
    val showComment: StateFlow<Boolean> = _showComment
    val pointComments: StateFlow<List<Comment>?> = _pointComments
    val newComment: StateFlow<Comment?> = _newComment
    val userManager = UserManager(context = context)

    init {
        _point.value = EasyPoint(
            commentId = 0,
            userId = 0,
            name = "",
            type = "",
            info = "",
            location = "",
            photo = ("https://27142293.s21i.faiusr.com/2/ABUIABACGAAg_I_bmQYokt25kQUwwAc4gAU.jpg".toUri()),
            refreshTime = "2024-12-29",
            likes = 100,
            dislikes = 10,
            lat = 37.7749,
            lng = -122.4194,
            pointId = 0
        )
        _newComment.value = Comment(
            comment_id = 1,
            userId = 0,
            content = "This is a comment by Bob",
            like = 5,
            dislike = 0,
            date = "2024-12-30",
            index = 2
        )
    }

    fun getPoint(marker: Marker) {
        viewModelScope.launch(Dispatchers.IO) {
            _point.value = repository.getPointFromMarker(marker)
            _point.value?.let {
                _pointComments.value = repository.getAllCommentsById(it.commentId)
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
        _newComment.value!!.content = string
    }

    fun publish() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadComment(
                _newComment.value!!.content, commentId = _point.value!!.commentId
            )
        }
    }

}

sealed interface CommentCardScreen {
    data object Comment : CommentCardScreen
    data object History : CommentCardScreen
}