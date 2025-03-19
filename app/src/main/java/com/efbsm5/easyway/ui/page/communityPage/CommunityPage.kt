package com.efbsm5.easyway.ui.page.communityPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.viewmodel.pageViewmodel.DetailPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.NewPostPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.ShowPageViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CommunityPage() {
    var state: State by remember { mutableStateOf(State.Community) }
    when (state) {
        State.Community -> {
            val showPageViewModel: ShowPageViewModel = koinViewModel()
            ShowPage(
                onChangeState = { state = State.NewPost },
                onSelectedPost = { state = State.Detail(it) },
                viewModel = showPageViewModel
            )
        }

        is State.Detail -> {
            val detailPageViewModel: DetailPageViewModel =
                koinViewModel(parameters = { parametersOf((state as State.Detail).dynamicPost) })
            DetailPage(
                onBack = { state = State.Community }, viewModel = detailPageViewModel
            )
        }

        State.NewPost -> {
            val newPostPageViewModel: NewPostPageViewModel = koinViewModel()
            NewDynamicPostPage(
                back = { state = State.Community }, viewModel = newPostPageViewModel
            )
        }
    }
}


sealed interface State {
    data object Community : State
    data object NewPost : State
    data class Detail(val dynamicPost: DynamicPost) : State
}