package com.efbsm5.easyway.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import com.efbsm5.easyway.data.models.Photo
import com.efbsm5.easyway.data.models.assistModel.DynamicPostAndUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShowPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _posts = MutableStateFlow<List<DynamicPostAndUser>>(emptyList())
    private var _photos = MutableStateFlow<List<Uri>>(emptyList())
    val posts: StateFlow<List<DynamicPostAndUser>> = _posts
    val photos: StateFlow<List<Uri>> = _photos

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val postList = repository.getAllDynamicPosts()
            val list = emptyList<DynamicPostAndUser>().toMutableList()
            postList.forEach {
                list.add(
                    DynamicPostAndUser(
                        dynamicPost = it,
                        user = repository.getUserById(it.userId),
                        commentCount = repository.getCommentCount(it.commentId)
                    )
                )
            }
            _posts.value = list.toList()
        }
    }

}

