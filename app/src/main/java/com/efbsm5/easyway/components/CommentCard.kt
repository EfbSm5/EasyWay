package com.efbsm5.easyway.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.R
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.database.getCommentByCommentId
import com.efbsm5.easyway.database.getUserByUserId


@Composable
fun CommentAndHistoryCard(points: EasyPoint) {
    var state: Screen by remember { mutableStateOf(Screen.Comment) }
    FacilityDetailScreen(points = points, screen = state, onChangeScreen = { state = it })
}

@Composable
fun FacilityDetailScreen(points: EasyPoint, screen: Screen, onChangeScreen: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PointInfo(points)
        Spacer(modifier = Modifier.height(16.dp))
        SelectPoint { onChangeScreen(it) }
        when (screen) {
            Screen.Comment -> CommentCard(
                onComment = {}, commentId = points.commentId
            )

            Screen.History -> HistoryCard()
        }
        Spacer(modifier = Modifier.height(16.dp))
        BottomActionBar()
    }
}

@Composable
fun PointInfo(points: EasyPoint) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.Gray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = points.photo), contentDescription = null
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "设施类别:${points.type}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "标注来源:", fontSize = 14.sp, color = Color.Gray)
            Text(text = "更新日期:${points.refreshTime}", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "👍 次数:${points.likes}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "👎 次数:${points.dislikes}", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "设施说明:", fontWeight = FontWeight.Bold, fontSize = 16.sp
    )
    Text(
        text = points.info, fontSize = 14.sp, color = Color.Gray
    )
}

@Composable
fun SelectPoint(onClick: (Screen) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "评论次数",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                onClick(Screen.Comment)
            })
        Spacer(Modifier.width(50.dp))
        Text(text = "历史版本",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                onClick(Screen.History)
            })
    }
}

@Composable
fun CommentCard(commentId: Int, onComment: () -> Unit) {
    val comments = getCommentByCommentId(LocalContext.current, commentId)
    if (comments.isNullOrEmpty()) {
        Text("暂无")
    } else LazyColumn {
        items(comments) {
            CommentItem(it, onComment = {})
        }
    }
}

@Composable
fun CommentItem(comment: Comment, onComment: () -> Unit) {
    val user = getUserByUserId(LocalContext.current, comment.userId)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(user?.avatar ?: R.drawable.nouser),
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape),
            contentDescription = "头像"
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "用户:${
                        user?.name ?: "用户不存在"
                    }", style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))

            }
            Text(
                text = comment.content,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "时间${comment.date}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "点赞",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Green
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "次数:${comment.like}", style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "点踩",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "次数:${comment.dislike}", style = MaterialTheme.typography.bodySmall
                )
            }
            Row {
                Button(onClick = { onComment() }) { Text("评论") }
            }
        }
    }
}

@Composable
fun HistoryCard() {
    // 历史卡片内容,目前懒得开发
}

@Composable
fun BottomActionBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { /* 更新内容 */ }, modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(text = "更新内容")
        }

        Spacer(modifier = Modifier.width(16.dp))

        OutlinedButton(
            onClick = { /* 发布评论 */ }, modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(text = "发布评论")
        }
    }
}

sealed interface Screen {
    data object Comment : Screen
    data object History : Screen
}