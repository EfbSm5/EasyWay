package com.efbsm5.easyway.ui.page.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageState
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageViewModel

@Composable
fun HomePage(viewModel: HomePageViewModel) {
    val points by viewModel.points.collectAsState()
    val posts by viewModel.post.collectAsState()
    val user = viewModel.user
    val state by viewModel.content.collectAsState()
    HomePageScreen(
        onUpdate = {
            viewModel.updateData()
        }, user = user,
        state = state,
        onChangeState = { viewModel.changeState(it) }
    )
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





