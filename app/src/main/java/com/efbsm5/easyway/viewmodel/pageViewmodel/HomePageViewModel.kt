package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.repository.DataRepository
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.network.IntentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomePageViewModel(
    val repository: DataRepository,
    val userManager: UserManager,
    val intentRepository: IntentRepository
) : ViewModel() {
    lateinit var user: User

    //    private val _points = MutableStateFlow(emptyList<EasyPoint>())
//    private val _post = MutableStateFlow(emptyList<DynamicPost>())
    private val _content = MutableStateFlow<HomePageState>(HomePageState.Main)
    val points: StateFlow<List<EasyPoint>> = repository.getPointByUserId(userManager.userId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val post: StateFlow<List<DynamicPost>> = repository.getPostByUserId(userManager.userId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val comment: StateFlow<List<Comment>> = repository.getCommentByUserId(userManager.userId)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val content: StateFlow<HomePageState> = _content

    init {
        viewModelScope.launch(Dispatchers.IO) {
            user = repository.getUserById(userManager.userId)
        }
    }

//    fun getUserPoint() {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getPointByUserId(user.id).collect {
//                _points.value = it
//            }
//        }
//    }
//
//    fun getUserPost() {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getPostByUserId(user.id).collect {
//                _post.value = it
//            }
//        }
//    }

    fun editUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
//            repository.
        }
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            intentRepository.syncData()
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
    data object EditUser : HomePageState
    data object Reg : HomePageState
    data object Version : HomePageState
    data object Help : HomePageState
    data object Settings : HomePageState
}