package com.efbsm5.easyway.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.R
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import com.efbsm5.easyway.viewmodel.ShowPageViewModel
import com.efbsm5.easyway.viewmodel.ViewModelFactory

@Composable
fun ShowPage(
    onChangeState: (State) -> Unit, onSelectedPost: (DynamicPost) -> Unit
) {
    val context = LocalContext.current
    val showPageViewModel = viewModel<ShowPageViewModel>(factory = ViewModelFactory(context))
    val postList = showPageViewModel.posts.collectAsState().value
    var text by remember { mutableStateOf("") }
    val tabs = listOf("全部", "活动", "互助", "分享")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    ShowPageScreen(posts = postList,
        onChangeState = { onChangeState(it) },
        text = text,
        onChangeText = { text = it },
        selectedTabIndex = selectedTabIndex,
        tabs = tabs,
        viewModel = showPageViewModel,
        onSelect = { selectedTabIndex = it },
        titleText = "心无距离，共享每一刻",
        onClick = {
            onSelectedPost(it)
        })
}

@Composable
fun ShowPageScreen(
    posts: List<DynamicPost>?,
    selectedTabIndex: Int,
    text: String,
    tabs: List<String>,
    titleText: String,
    viewModel: ShowPageViewModel,
    onChangeText: (String) -> Unit,
    onChangeState: (State) -> Unit,
    onSelect: (Int) -> Unit,
    onClick: (DynamicPost) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(text = titleText)
        BannerSection()
        SearchBar(text = text, onChangeText = { onChangeText(it) })
        TabSection(selectedTabIndex = selectedTabIndex, tabs = tabs, onSelect = { onSelect(it) })
        DynamicPostList(
            posts = posts, onClick = { onClick(it) },
        )
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(
            onClick = { onChangeState(State.NewPost) },
            modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "添加")
        }
    }
}

@Composable
fun TopBar(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun BannerSection() {
    Image(
        painter = painterResource(id = R.drawable.img),
        contentDescription = "活动横幅",
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
fun SearchBar(text: String, onChangeText: (String) -> Unit) {
    TextField(value = text,
        onValueChange = { onChangeText(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp)),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                modifier = Modifier.padding(8.dp)
            )
        },
        placeholder = { Text("搜索") })
}

@Composable
fun TabSection(selectedTabIndex: Int, tabs: List<String>, onSelect: (Int) -> Unit) {
    TabRow(selectedTabIndex = selectedTabIndex) {
        tabs.forEachIndexed { index, title ->
            Tab(selected = index == selectedTabIndex, onClick = { onSelect(index) }) {
                Text(
                    text = title, modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun DynamicPostList(
    posts: List<DynamicPost>?, onClick: (DynamicPost) -> Unit
) {
    if (posts.isNullOrEmpty()) {
        Text("没有数据")
    } else {
        LazyColumn {
            items(posts) {
                PostsItem(it) { onClick(it) }
            }
        }
    }
}


@Composable
private fun PostsItem(dynamicPost: DynamicPost, onClick: () -> Unit) {
    val context = LocalContext.current
    val repository = DataRepository(context)
    var user by remember { mutableStateOf<User?>(null) }
    LaunchedEffect(dynamicPost.userId) {
        user = repository.getUserById(dynamicPost.userId)
    }
    var commentsCount by remember { mutableIntStateOf(0) }
    LaunchedEffect(dynamicPost.commentId) {
        val comments = repository.getAllCommentsById(dynamicPost.commentId)
        commentsCount = comments.size
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable { onClick() }) {
        Image(
            rememberAsyncImagePainter(user?.avatar ?: R.drawable.nouser),
            contentDescription = "头像",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = user?.name ?: "此用户不存在", style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "#分享", color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = dynamicPost.date, color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (dynamicPost.content.length > 15) dynamicPost.content.take(15) + "..." else dynamicPost.content,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Image(
                rememberAsyncImagePainter(
                    dynamicPost.photos.first().url
                ),
                contentDescription = "评论图片",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "点赞",
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = dynamicPost.like.toString(),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "评论 ${commentsCount.toString()}")
            }
        }
    }
}
