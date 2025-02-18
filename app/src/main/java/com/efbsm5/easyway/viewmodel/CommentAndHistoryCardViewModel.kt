package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.EasyPoint
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
    private var _newComment = MutableStateFlow(Comment())
    val point: StateFlow<EasyPoint?> = _point
    val state: StateFlow<CommentCardScreen> = _state
    val showComment: StateFlow<Boolean> = _showComment
    val pointComments: StateFlow<List<Comment>?> = _pointComments
    val newComment: StateFlow<Comment> = _newComment

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
        _newComment.value.content = string
    }

    fun publish() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadComment(_newComment.value)
        }
    }

}

sealed interface CommentCardScreen {
    data object Comment : CommentCardScreen
    data object History : CommentCardScreen
}