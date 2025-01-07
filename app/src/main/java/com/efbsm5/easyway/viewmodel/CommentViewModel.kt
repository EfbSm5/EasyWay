package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CommentViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private val _comments = MutableStateFlow<List<Comment>?>(null)
    val comments: StateFlow<List<Comment>?> = _comments

    fun fetchCommentsById(commentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _comments.value = repository.getAllCommentsById(commentId)
        }
    }
}