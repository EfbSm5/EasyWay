package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.User
import com.efbsm5.easyway.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> =_user

    fun fetchUserById(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.value = repository.getUserById(userId = userId)
        }
    }
}