package com.efbsm5.easyway.ui.page.homepage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageState
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageViewModel

@Composable
fun HomePage(viewModel: HomePageViewModel) {
    val points by viewModel.points.collectAsState()
    val posts by viewModel.post.collectAsState()
    val user = viewModel.user
    val state by viewModel.content.collectAsState()
    when (state) {
        HomePageState.Main -> {
            MainPageScreen(user = user, changeState = { viewModel.changeState(it) })
        }

        HomePageState.Point -> TODO()
        HomePageState.Post -> TODO()
        HomePageState.Comment -> TODO()
        HomePageState.EditUser -> TODO()
        HomePageState.Help -> TODO()
        HomePageState.Reg -> TODO()
        HomePageState.Settings -> TODO()
        HomePageState.Version -> TODO()
    }
}





