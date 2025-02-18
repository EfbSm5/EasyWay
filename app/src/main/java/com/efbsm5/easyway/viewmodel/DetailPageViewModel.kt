package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.User
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailPageViewModel(context: Context, private val dynamicPost: DynamicPost = DynamicPost()) :
    ViewModel() {
    private val repository = DataRepository(context)
    private val _dynamicPost = MutableStateFlow(dynamicPost)
    private var _comments = MutableStateFlow<List<Comment>?>(null)
    private var _users = MutableStateFlow(emptyList<User>().toMutableList())
    private var _newCommentText = MutableStateFlow("")
    private var _showTextField = MutableStateFlow(false)
    private var _postUser = MutableStateFlow(User())
    val users: StateFlow<MutableList<User>> = _users
    val comments: StateFlow<List<Comment>?> = _comments
    val newCommentText: StateFlow<String> = _newCommentText
    val showTextField: StateFlow<Boolean> = _showTextField
    val postUser: StateFlow<User> = _postUser

    init {
        getComment()
    }

    private fun getComment() {
        viewModelScope.launch(Dispatchers.IO) {
            _postUser.value = repository.getUserById(dynamicPost.userId)
            _comments.value =
                repository.getAllCommentsById(commentId = _dynamicPost.value.commentId)
            _comments.value?.forEach {
                _users.value.add(repository.getUserById(it.userId))
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