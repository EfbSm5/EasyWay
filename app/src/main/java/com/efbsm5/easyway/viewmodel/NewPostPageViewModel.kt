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

class NewPostPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _newPost = MutableStateFlow(
        DynamicPost(
            title = "DynamicPost 1",
            date = "2024-12-29",
            like = 20,
            content = "Content 1",
            lat = 30.5155,
            lng = 114.4268,
            position = "Position 1",
            userId = 1,
            commentId = 1,
            id = 1,
            photos = emptyList()
        )
    )
    private var _selectedButton = MutableStateFlow("")
    val newPost: StateFlow<DynamicPost> = _newPost
    val selectedButton: StateFlow<String> = _selectedButton

    fun changeSelectedButton(string: String) {
        _selectedButton.value = string
    }

    fun editPost(dynamicPost: DynamicPost) {
        _newPost.value = dynamicPost
    }

    fun push() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadPost(_newPost.value)
        }
    }
}