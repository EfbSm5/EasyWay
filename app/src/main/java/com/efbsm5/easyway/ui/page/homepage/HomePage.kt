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
    HomePageScreen(onUpdate = {
        viewModel.updateData()
    }, user = user, state = state, onChangeState = { viewModel.changeState(it) })
}

@Composable
private fun HomePageScreen(
    user: User, onUpdate: () -> Unit, state: HomePageState, onChangeState: (HomePageState) -> Unit
) {
    when (state) {
        HomePageState.Main -> {
            HomePageMain(
                onUpdate = onUpdate, user = user, changeState = { onChangeState(it) })
        }

        HomePageState.Point -> TODO()
        HomePageState.Post -> TODO()
        HomePageState.Comment -> TODO()
    }
}





