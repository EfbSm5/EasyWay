package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import com.efbsm5.easyway.data.models.assistModel.CommentAndUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentAndHistoryCardViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _state = MutableStateFlow<CommentCardScreen>(CommentCardScreen.Comment)
    private var _point = MutableStateFlow<EasyPoint?>(null)
    private var _showComment = MutableStateFlow(false)
    private var _pointComments = MutableStateFlow<List<CommentAndUser>?>(null)
    private var _newComment = MutableStateFlow("")
    val point: StateFlow<EasyPoint?> = _point
    val state: StateFlow<CommentCardScreen> = _state
    val showComment: StateFlow<Boolean> = _showComment
    val pointComments: StateFlow<List<CommentAndUser>?> = _pointComments
    val newComment: StateFlow<String> = _newComment

    fun getPoint(marker: Marker) {
        viewModelScope.launch(Dispatchers.IO) {
            var comments = emptyList<Comment>()
            val commentsAndUser = emptyList<CommentAndUser>().toMutableList()
            _point.value = repository.getPointFromMarker(marker)
            _point.value?.let {
                comments = repository.getAllCommentsById(it.commentId)
            }
            comments.forEach {
                commentsAndUser.add(
                    CommentAndUser(
                        repository.getUserById(it.userId), it
                    )
                )
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

}

sealed interface CommentCardScreen {
    data object Comment : CommentCardScreen
    data object History : CommentCardScreen
}