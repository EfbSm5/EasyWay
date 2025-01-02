package com.efbsm5.easyway.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.data.DynamicPost

@Composable
fun DetailPage(post: DynamicPost) {
    DetailPageScreen(post)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPageScreen(post: DynamicPost) {
    TopAppBar(title = { Text("详情页") }, navigationIcon = {
        IconButton(onClick = { /* 返回逻辑 */ }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    })
    Column(
        modifier = Modifier
            .padding()
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    rememberAsyncImagePainter(post.user.avatar),
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(post.user.name, fontWeight = FontWeight.Bold)
                    Text(post.date, color = Color.Gray)
                }
            }
            Text("你好")
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                rememberAsyncImagePainter(post.photos[0]),
                contentDescription = "Post Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Icon(Icons.Default.ThumbUp, contentDescription = "Like")
                Spacer(modifier = Modifier.width(8.dp))
                Text(post.like.toString())
                Spacer(modifier = Modifier.width(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(post.comment) { comment ->
                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        rememberAsyncImagePainter(comment.user.avatar),
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row {
                            Text(comment.user.name, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                comment.date, color = Color.Gray
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ThumbUp,
                        contentDescription = "Like",
                        modifier = Modifier.clickable { })
                }
            }
        }
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Write Comment")
            Spacer(modifier = Modifier.width(8.dp))
            Text("写回复", color = Color.Gray)
        }
    }
}