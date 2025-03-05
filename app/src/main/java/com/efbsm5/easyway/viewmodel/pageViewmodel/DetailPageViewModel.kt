package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import com.efbsm5.easyway.data.models.assistModel.CommentAndUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private lateinit var _dynamicPost: MutableStateFlow<DynamicPost>
    private var _newCommentText = MutableStateFlow("")
    private var _showTextField = MutableStateFlow(false)
    private var _postUser = MutableStateFlow(
        User(
            id = 0, name = "test", avatar = null
        )
    )
    private val _commentAndUsers = MutableStateFlow(emptyList<CommentAndUser>().toMutableList())
    private val _photos = MutableStateFlow(emptyList<Uri>().toMutableList())
    val newCommentText: StateFlow<String> = _newCommentText
    val showTextField: StateFlow<Boolean> = _showTextField
    val postUser: StateFlow<User> = _postUser
    val commentAndUser: StateFlow<List<CommentAndUser>> = _commentAndUsers
    val photos: StateFlow<List<Uri>> = _photos
    val post: StateFlow<DynamicPost> = _dynamicPost

    fun getPost(dynamicPost: DynamicPost) {
        _dynamicPost.value = dynamicPost
        getData()
    }

    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            _postUser.value = repository.getUserById(post.value.userId)
            val comments = repository.getAllCommentsById(commentId = _dynamicPost.value.commentId)
            comments.forEach {
                _commentAndUsers.value.add(CommentAndUser(repository.getUserById(it.userId), it))
            }
            val photos = repository.getAllPhotosById(post.value.photoId)
            photos.forEach { _photos.value.add(it.uri) }
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