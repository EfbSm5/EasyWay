package com.efbsm5.easyway.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.efbsm5.easyway.data.DynamicPost

@Composable
fun CommunityPage() {
//    var communityState: CommunityState by remember { mutableStateOf(CommunityState.Community) }
//    when (communityState) {
//        CommunityState.Community -> ShowPage(onChangeState = { communityState = it },
//            onSelectedPost = { communityState = CommunityState.Detail(it) })
//
//        is CommunityState.Detail -> DetailPage((communityState as CommunityState.Detail).dynamicPost)
//        CommunityState.NewPost -> NewDynamicPostPage()
//    }
}


sealed interface CommunityState {
    data object Community : CommunityState
    data object NewPost : CommunityState
    data class Detail(val dynamicPost: DynamicPost) : CommunityState
}