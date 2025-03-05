package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentAndHistoryCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPlaceCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPointCardViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.DetailPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.NewPostPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.ShowPageViewModel

class ViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MapPageViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(DetailPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return DetailPageViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(NewPostPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return NewPostPageViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(CommentAndHistoryCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return CommentAndHistoryCardViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(NewPointCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return NewPointCardViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(ShowPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ShowPageViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(NewPlaceCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return NewPlaceCardViewModel() as T
        }
        if (modelClass.isAssignableFrom(HomePageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return HomePageViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return create(modelClass)
    }
}