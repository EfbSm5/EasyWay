package com.efbsm5.easyway.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.efbsm5.easyway.R



@Composable
fun CommunityPage() {

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar()
        BannerSection()
        SearchBar()
        TabSection()
        CommentList()
    }
}

@Composable
fun TopBar() {
    Text(
        text = "心无距离，共享每一刻",
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
fun SearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "搜索",
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "搜索", color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun TabSection() {
    val tabs = listOf("全部", "活动", "互助", "分享")
    var selectedTabIndex by remember { mutableStateOf(0) }
    TabRow(selectedTabIndex = selectedTabIndex) {
        tabs.forEachIndexed { index, title ->
            Tab(selected = index == selectedTabIndex, onClick = { selectedTabIndex = index }) {
                Text(
                    text = title, modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun CommentList() {
    LazyColumn {
        items(5) { // 示例：显示5条评论
            CommentItem()
        }
    }
}

@Composable
fun CommentItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img), // 替换为头像资源
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
                    text = "清障志愿者 No.249", style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "#分享", color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = "2024-12-23", color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "你好",
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.img), // 替换为评论图片资源
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
                Text(text = "0", modifier = Modifier.padding(horizontal = 4.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "评论 0")
            }
        }
    }
}