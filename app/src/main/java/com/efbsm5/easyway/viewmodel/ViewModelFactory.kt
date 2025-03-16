package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.network.IntentRepository
import com.efbsm5.easyway.data.repository.DataRepository
import com.efbsm5.easyway.map.LocationSaver
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentAndHistoryCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPointCardViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.DetailPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.NewPostPageViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.FunctionCardViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.ShowPageViewModel

class ViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {
    private val repository: DataRepository = DataRepository(context)
    private val userManager: UserManager = UserManager(context)
    private val intentRepository = IntentRepository(context)
    private val locationSaver = LocationSaver(context)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return MapPageViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(DetailPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return DetailPageViewModel(repository, userManager) as T
        }
        if (modelClass.isAssignableFrom(NewPostPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return NewPostPageViewModel(
                repository,
                locationSaver = locationSaver
            ) as T
        }
        if (modelClass.isAssignableFrom(CommentAndHistoryCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return CommentAndHistoryCardViewModel(
                repository, userManager
            ) as T
        }
        if (modelClass.isAssignableFrom(NewPointCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return NewPointCardViewModel(repository, locationSaver) as T
        }
        if (modelClass.isAssignableFrom(ShowPageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return ShowPageViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(HomePageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return HomePageViewModel(
                repository, userManager, intentRepository
            ) as T
        }
        if (modelClass.isAssignableFrom(FunctionCardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return FunctionCardViewModel(repository, locationSaver) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return create(modelClass)
    }
}