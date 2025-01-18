package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.database.AppDataBase
import com.efbsm5.easyway.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DynamicPostViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private val _dynamicPosts = MutableStateFlow<List<DynamicPost>?>(null)
    val dynamicPosts: StateFlow<List<DynamicPost>?> = _dynamicPosts

    fun fetchDynamicPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            _dynamicPosts.value = repository.getAllDynamicPosts()
        }
    }

    fun insertDynamicPost(post: DynamicPost, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context)
            database.dynamicPostDao().insert(post)
        }
    }
}