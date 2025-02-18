package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShowPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _posts = MutableStateFlow<List<DynamicPost>>(emptyList())
    val posts: StateFlow<List<DynamicPost>> = _posts

    init {
        fetchPosts()
    }

    private fun fetchPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val postList = repository.getAllDynamicPosts()
            _posts.value = postList
        }
    }

    fun fetchUser() {

    }

}

