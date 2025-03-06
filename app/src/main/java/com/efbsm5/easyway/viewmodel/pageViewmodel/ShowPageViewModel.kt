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
    private val _selectedTab = MutableStateFlow(0)
    private val _text = MutableStateFlow("")
    val text: StateFlow<String> = _text
    val selectedTab: StateFlow<Int> = _selectedTab
    val posts: StateFlow<List<DynamicPostAndUser>> = _posts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllDynamicPosts().collect { dynamicPosts ->
                val list = emptyList<DynamicPostAndUser>().toMutableList()
                dynamicPosts.forEach { post ->
                    repository.getCommentCount(post.commentId).collect {
                        list.clear()
                        list.add(
                            DynamicPostAndUser(
                                dynamicPost = post,
                                user = repository.getUserById(post.userId),
                                commentCount = it,
                                photo = repository.getAllPhotosById(post.photoId)
                            )
                        )
                        _posts.value = list.toList()
                    }
                }
            }
        }
    }

    fun changeTab(int: Int) {
        _selectedTab.value = int
    }

    fun changeText(string: String) {
        _text.value = string
    }
}

