package com.efbsm5.easyway.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.efbsm5.easyway.data.DynamicPost

@Composable
fun CommunityPage() {
    var state: State by remember { mutableStateOf(State.Community) }
    when (state) {
        State.Community -> ShowPage(onChangeState = { state = it },
            onSelectedPost = { state = State.Detail(it) })

        is State.Detail -> DetailPage(
            (state as State.Detail).dynamicPost,
            onBack = { state = State.Community })

        State.NewPost -> NewDynamicPostPage(navigate = { state = State.Community })
    }
}


sealed interface State {
    data object Community : State
    data object NewPost : State
    data class Detail(val dynamicPost: DynamicPost) : State
}