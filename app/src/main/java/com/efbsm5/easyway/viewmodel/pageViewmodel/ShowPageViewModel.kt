package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.repository.DataRepository
import com.efbsm5.easyway.data.models.assistModel.DynamicPostAndUser
import com.efbsm5.easyway.data.models.assistModel.PostType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShowPageViewModel(val repository: DataRepository) : ViewModel() {
    private var _posts = MutableStateFlow<List<DynamicPostAndUser>>(emptyList())
    private var _showPosts = MutableStateFlow<List<DynamicPostAndUser>>(emptyList())
    private val _selectedTab = MutableStateFlow(0)
    val posts: StateFlow<List<DynamicPostAndUser>> = _showPosts

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
                            )
                        )
                        _posts.value = list.toList()
                        _showPosts.value = list.toList()
                    }
                }
            }
        }
    }

    fun changeTab(int: Int) {
        _selectedTab.value = int
        if (int != PostType.ALL) {
            Log.e("", "changeTab: $int")
            _showPosts.value = _posts.value.filter {
                it.dynamicPost.type == int
            }
        } else {
            _showPosts.value = _posts.value
        }
    }

    fun search(string: String) {
        _showPosts.value = _posts.value.filter {
            it.dynamicPost.title.contains(string)
        }
    }
}

