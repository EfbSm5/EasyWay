package com.efbsm5.easyway.ui.page.homepage

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.efbsm5.easyway.map.MapUtil.showMsg
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageState
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageViewModel

@Composable
fun HomePage(viewModel: HomePageViewModel) {
    val points by viewModel.points.collectAsState()
    val posts by viewModel.post.collectAsState()
    val user by viewModel.user.collectAsState()
    val state by viewModel.content.collectAsState()
    val context = LocalContext.current
    when (state) {
        HomePageState.Comment -> {
            MainPageScreen(user, viewModel::changeState)
            showMsg("仍在开发", context)
        }

        HomePageState.EditUser -> {
            MainPageScreen(user, viewModel::changeState)
            showMsg("仍在开发", context)
        }

        HomePageState.Help -> InfoScreen()
        HomePageState.Main -> MainPageScreen(user, viewModel::changeState)

        HomePageState.Point -> {
            MainPageScreen(user, viewModel::changeState)
            showMsg("仍在开发", context)
        }

        HomePageState.Post -> {
            viewModel.getUserPost()
            ShowPostPage(posts)
        }

        HomePageState.Reg -> {
            MainPageScreen(user, viewModel::changeState)
            showMsg("仍在开发", context)
        }

        HomePageState.Settings -> SettingsScreen(viewModel::changeState)
        HomePageState.Version -> {
            MainPageScreen(user, viewModel::changeState)
            showMsg("仍在开发,预计实现关爱版", context)
        }
    }
    BackHandler(
        enabled = state != HomePageState.Main, onBack = {
            viewModel.changeState(HomePageState.Main)
        })

}





