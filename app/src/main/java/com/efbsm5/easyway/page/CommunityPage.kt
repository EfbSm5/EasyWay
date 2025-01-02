package com.efbsm5.easyway.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.R
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.getPostDatas

@Composable
fun CommunityPage() {
    var state: State by remember { mutableStateOf(State.Community) }
    var selectedPost: DynamicPost? by remember { mutableStateOf(null) }
    when (state) {
        State.Community -> ShowPage(onChangeState = { state = it },
            onSelectedPost = { selectedPost = it })

        State.Detail -> DetailPage(selectedPost!!)
        State.NewPost -> DynamicPostPage()
    }
}


@Composable
fun ShowPage(onChangeState: (State) -> Unit, onSelectedPost: (DynamicPost) -> Unit) {
    var text by remember { mutableStateOf("") }
    val tabs = listOf("全部", "活动", "互助", "分享")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val posts = getPostDatas()
    ShowPageScreen(onChangeState = { onChangeState(it) },
        text = text,
        onChangeText = { text = it },
        selectedTabIndex = selectedTabIndex,
        tabs = tabs,
        onSelect = { selectedTabIndex = it },
        titleText = "心无距离，共享每一刻",
        posts = posts,
        onClick = {
            onSelectedPost(it)
            onChangeState(State.Detail)
        })
}

@Composable
fun ShowPageScreen(
    selectedTabIndex: Int,
    text: String,
    tabs: List<String>,
    titleText: String,
    posts: ArrayList<DynamicPost>,
    onChangeText: (String) -> Unit,
    onChangeState: (State) -> Unit,
    onSelect: (Int) -> Unit,
    onClick: (DynamicPost) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(text = titleText)
        BannerSection()
        SearchBar(text = text, onChangeText = { onChangeText(it) })
        TabSection(selectedTabIndex = selectedTabIndex, tabs = tabs, onSelect = { onSelect(it) })
        CommentList(posts = posts, onClick = { onClick(it) })
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
fun CommentList(posts: ArrayList<DynamicPost>, onClick: (DynamicPost) -> Unit) {
    LazyColumn {
        items(posts) {
            CommentItem(it) { onClick(it) }
        }
    }
}

@Composable
fun CommentItem(dynamicPost: DynamicPost, onClick: () -> Unit) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .clickable { onClick() }) {
        Image(
            rememberAsyncImagePainter(dynamicPost.user.avatar),
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
                    text = dynamicPost.user.name, style = MaterialTheme.typography.bodyLarge
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
            if (dynamicPost.photos.isNotEmpty()) {
                Image(
                    rememberAsyncImagePainter(
                        dynamicPost.photos
                    ),
                    contentDescription = "评论图片",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
            }

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
                Text(text = "评论 ${dynamicPost.comment.size}")
            }
        }
    }
}

sealed interface State {
    data object Community : State
    data object NewPost : State
    data object Detail : State
}