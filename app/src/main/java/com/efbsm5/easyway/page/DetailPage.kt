package com.efbsm5.easyway.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.efbsm5.easyway.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPage() {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(title = { Text("详情页") }, navigationIcon = {
            IconButton(onClick = { /* 返回逻辑 */ }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        })

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
                    painter = painterResource(id = R.drawable.user_avatar), // 替换为用户头像资源
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text("清障志愿者 No.249", fontWeight = FontWeight.Bold)
                    Text("2024-12-23", style = MaterialTheme.typography.body2, color = Color.Gray)
                }
            }
            Text("你好", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Image(
                painter = painterResource(id = R.drawable.post_image), // 替换为帖子图片资源
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
                Text("0")
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Default.ThumbDown, contentDescription = "Dislike")
                Spacer(modifier = Modifier.width(8.dp))
                Text("0")
            }
        }

        // 评论区
        Divider(color = Color.Gray, thickness = 1.dp)
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(listOf("你也好", "你真好")) { comment ->
                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.user_avatar), // 替换为用户头像资源
                        contentDescription = "User Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Row {
                            Text("用户XXX", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "2024.12.23",
                                style = MaterialTheme.typography.body2,
                                color = Color.Gray
                            )
                        }
                        Text(comment, style = MaterialTheme.typography.body1)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ThumbUp, contentDescription = "Like")
                }
            }
        }

        // 底部输入栏
        Divider(color = Color.Gray, thickness = 1.dp)
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
