package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import com.efbsm5.easyway.data.models.assistModel.DynamicPostAndUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShowPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _posts = MutableStateFlow<List<DynamicPostAndUser>>(emptyList())
    val posts: StateFlow<List<DynamicPostAndUser>> = _posts

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
                        commentCount = repository.getCommentCount(it.commentId),
                        photo = repository.getAllPhotosById(it.photoId)
                    )
                )
            }
            _posts.value = list.toList()
        }
    }

}

