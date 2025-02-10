package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CommentAndHistoryCardViewModel(context: Context) : ViewModel() {
//    private var _state = MutableStateFlow < CommentCardScreen > (CommentCardScreen.Comment)
//    val state: StateFlow<CommentCardScreen> = _state
}

sealed interface CommentCardScreen {
    data object Comment : Screen
    data object History : Screen
}