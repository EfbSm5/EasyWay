package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewPostPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _newPost = MutableStateFlow(DynamicPost())
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

        }
    }
}