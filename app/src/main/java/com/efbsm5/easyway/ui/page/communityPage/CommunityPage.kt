package com.efbsm5.easyway.ui.page.communityPage

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.viewmodel.ViewModelFactory
import com.efbsm5.easyway.viewmodel.pageViewmodel.DetailPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.NewPostPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.ShowPageViewModel

@Composable
fun CommunityPage() {
    var state: State by remember { mutableStateOf(State.Community) }
    val context = LocalContext.current
    val showPageViewModel =
        viewModel<ShowPageViewModel>(factory = ViewModelFactory(context = context))
    val detailPageViewModel = viewModel<DetailPageViewModel>(factory = ViewModelFactory(context))
    val newPostPageViewModel = viewModel<NewPostPageViewModel>(factory = ViewModelFactory(context))
    when (state) {
        State.Community -> ShowPage(
            onChangeState = { state = State.NewPost },
            onSelectedPost = { state = State.Detail(it) },
            viewModel = showPageViewModel
        )

        is State.Detail -> {
            detailPageViewModel.getPost((state as State.Detail).dynamicPost)
            DetailPage(
                onBack = { state = State.Community }, viewModel = detailPageViewModel
            )
        }

        State.NewPost -> NewDynamicPostPage(
            back = { state = State.Community }, viewModel = newPostPageViewModel
        )
    }
}


sealed interface State {
    data object Community : State
    data object NewPost : State
    data class Detail(val dynamicPost: DynamicPost) : State
}