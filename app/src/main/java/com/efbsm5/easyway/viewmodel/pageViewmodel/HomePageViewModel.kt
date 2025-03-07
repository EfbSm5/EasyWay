package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.network.IntentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomePageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private val repo = IntentRepository(context)
    private val userManager = UserManager(context)
    lateinit var user: User
    private val _points = MutableStateFlow(emptyList<EasyPoint>())
    private val _post = MutableStateFlow(emptyList<DynamicPost>())
    private val _content = MutableStateFlow<HomePageState>(HomePageState.Main)
    val points: StateFlow<List<EasyPoint>> = _points
    val post: StateFlow<List<DynamicPost>> = _post
    val content: StateFlow<HomePageState> = _content


    init {
        viewModelScope.launch(Dispatchers.IO) {
            user = repository.getUserById(userManager.userId)
        }
    }

    fun getUserPoint() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPointByUserId(user.id).collect {
                _points.value = it
            }
        }
    }

    fun getUserPost() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPostByUserId(user.id).collect {
                _post.value = it
            }
        }
    }

    fun editUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
//            repository.
        }
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.syncData()
        }
    }

    fun changeState(homePageState: HomePageState) {
        _content.value = homePageState
    }
}

sealed interface HomePageState {
    data object Main : HomePageState
    data object Post : HomePageState
    data object Point : HomePageState
    data object Comment : HomePageState
}