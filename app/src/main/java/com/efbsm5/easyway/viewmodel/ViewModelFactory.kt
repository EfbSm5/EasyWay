package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras

class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PointsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return PointsViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(DynamicPostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return DynamicPostViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(CommentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return CommentViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return UserViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(MapPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MapPageViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(DetailPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return DetailPageViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return create(modelClass)
    }
}