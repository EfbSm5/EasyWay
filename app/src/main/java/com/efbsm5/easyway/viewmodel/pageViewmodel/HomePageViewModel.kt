package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository

class HomePageViewModel(context: Context) : ViewModel() {
    private val user = UserManager(context)
    private val repository = DataRepository(context)

    fun getUserPoint() {
//repository.
    }
}