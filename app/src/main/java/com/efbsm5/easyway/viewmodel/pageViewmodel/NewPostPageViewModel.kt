package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.Context
import android.net.Uri
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
            title = "",
            date = "",
            like = 0,
            content = "",
            lat = 30.5155,
            lng = 114.4268,
            position = "",
            userId = 1,
            commentId = 1,
            id = 1,
            photoId = 1
        )
    )
    private var _selectedButton = MutableStateFlow("")
    private val _choosedPhotos = MutableStateFlow(emptyList<Uri>().toMutableList())
    val newPost: StateFlow<DynamicPost> = _newPost
    val selectedButton: StateFlow<String> = _selectedButton
    val choosedPhotos: StateFlow<List<Uri>> = _choosedPhotos

    fun changeSelectedButton(string: String) {
        _selectedButton.value = string
    }

    fun editPost(dynamicPost: DynamicPost) {
        _newPost.value = dynamicPost
    }

    fun getPicture(uri: Uri) {
        _choosedPhotos.value = _choosedPhotos.value.apply { add(uri) }.toMutableList()
    }
    fun push() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadPost(_newPost.value, _choosedPhotos.value)
        }
    }

    private fun updatePhotos(newPhotos: List<Uri>) {
        _choosedPhotos.value = newPhotos.toMutableList()
    }
}