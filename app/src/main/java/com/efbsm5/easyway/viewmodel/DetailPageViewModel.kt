package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import com.efbsm5.easyway.data.models.CommentAndUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailPageViewModel(context: Context, private val dynamicPost: DynamicPost? = null) :
    ViewModel() {
    private val repository = DataRepository(context)
    private val _dynamicPost = MutableStateFlow(dynamicPost)
    private var _newCommentText = MutableStateFlow("")
    private var _showTextField = MutableStateFlow(false)
    private var _postUser = MutableStateFlow<User?>(null)
    private val _commentAndUsers = MutableStateFlow(emptyList<CommentAndUser>().toMutableList())
    val newCommentText: StateFlow<String> = _newCommentText
    val showTextField: StateFlow<Boolean> = _showTextField
    val postUser: StateFlow<User?> = _postUser
    val commentAndUser: StateFlow<List<CommentAndUser>> = _commentAndUsers

    init {
        getComment()
    }

    private fun getComment() {
        viewModelScope.launch(Dispatchers.IO) {
            _postUser.value = repository.getUserById(dynamicPost!!.userId)
            val comments = repository.getAllCommentsById(commentId = _dynamicPost.value!!.commentId)
            comments.forEach {
                _commentAndUsers.value.add(CommentAndUser(repository.getUserById(it.userId), it))
            }

        }
    }

    fun addLike(commentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLike(commentId)
        }
    }


    fun changeText(text: String) {
        _newCommentText.value = text
    }

    fun ifShowTextField(boolean: Boolean) {
        _showTextField.value = boolean
    }
}