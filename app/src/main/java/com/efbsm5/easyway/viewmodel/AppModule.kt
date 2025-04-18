package com.efbsm5.easyway.viewmodel

import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.network.IntentRepository
import com.efbsm5.easyway.data.repository.DataRepository
import com.efbsm5.easyway.map.LocationSaver
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentAndHistoryCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.FunctionCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPointCardViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.DetailPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.NewPostPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.ShowPageViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { DataRepository() }
    single { LocationSaver }
    single { UserManager }
    single { IntentRepository }

    viewModel { MapPageViewModel(get()) }
    viewModel { (initialPost: DynamicPost) -> DetailPageViewModel(get(), get(), initialPost) }
    viewModel { NewPostPageViewModel(get(), get()) }
    viewModel { (initialPoint: EasyPoint) ->
        CommentAndHistoryCardViewModel(
            get(),
            get(),
            initialPoint
        )
    }
    viewModel { NewPointCardViewModel(get(), get()) }
    viewModel { ShowPageViewModel(get()) }
    viewModel { HomePageViewModel(get(), get(), get()) }
    viewModel { FunctionCardViewModel(get(), get()) }
}