package com.efbsm5.easyway.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.database.getAllDynamicPost

@Composable
fun CommunityPage() {
    val context = LocalContext.current
    var state: State by remember { mutableStateOf(State.Community) }
    var selectedPost: DynamicPost? by remember { mutableStateOf(null) }
    val posts = getAllDynamicPost(context)
    when (state) {
        State.Community -> ShowPage(
            onChangeState = { state = it },
            onSelectedPost = { selectedPost = it },
            posts = posts,
        )

        State.Detail -> DetailPage(selectedPost!!)
        State.NewPost -> NewDynamicPostPage()
    }
}



sealed interface State {
    data object Community : State
    data object NewPost : State
    data object Detail : State
}