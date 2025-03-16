package com.efbsm5.easyway.ui.page.homepage

import androidx.compose.animation.Crossfade
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
    val user = viewModel.user
    val state by viewModel.content.collectAsState()
    val context = LocalContext.current
    Crossfade(
        targetState = state
    ) {
        when (it) {
            HomePageState.Main -> {
                MainPageScreen(user = user, changeState = { viewModel.changeState(it) })
            }

            HomePageState.Point -> {
                MainPageScreen(user = user, changeState = { viewModel.changeState(it) })
                showMsg(
                    context = context, text = "待开发"
                )
            }

            HomePageState.Post -> {
                MainPageScreen(user = user, changeState = { viewModel.changeState(it) })
                showMsg(
                    context = context, text = "待开发"
                )
            }

            HomePageState.Comment -> {
                MainPageScreen(user = user, changeState = { viewModel.changeState(it) })
                showMsg(
                    context = context, text = "待开发"
                )
            }

            HomePageState.EditUser -> {
                MainPageScreen(user = user, changeState = { viewModel.changeState(it) })
                showMsg(
                    context = context, text = "待开发"
                )
            }

            HomePageState.Help -> {
                MainPageScreen(user = user, changeState = { viewModel.changeState(it) })
                showMsg(
                    context = context, text = "待开发"
                )
            }

            HomePageState.Reg -> {
                MainPageScreen(user = user, changeState = { viewModel.changeState(it) })
                showMsg(
                    context = context, text = "待开发"
                )
            }

            HomePageState.Settings -> SettingsScreen()
            HomePageState.Version -> {
                MainPageScreen(user = user, changeState = { viewModel.changeState(it) })
                showMsg(
                    context = context, text = "待开发"
                )
            }
        }

    }
}





