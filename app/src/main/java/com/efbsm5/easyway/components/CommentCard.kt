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
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.R
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.database.AppDataBase
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.efbsm5.easyway.data.User
import com.efbsm5.easyway.viewmodel.PointsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CommentAndHistoryCard(marker: Marker) {
    val context = LocalContext.current
    var state: Screen by remember { mutableStateOf(Screen.Comment) }
    var point by remember { mutableStateOf<EasyPoint?>(null) }
    var comments by remember { mutableStateOf(emptyList<Comment>()) }
//    val pointsViewModel: PointsViewModel = viewModel<PointsViewModel>()
//    val points by pointsViewModel.points.collectAsState()
    val scope = rememberCoroutineScope()
    LaunchedEffect(marker) {
        scope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context)
            val fetchedPoint = database.pointsDao()
                .getPointByLatLng(marker.position.latitude, marker.position.longitude)
            fetchedPoint?.let {
                val fetchedComments = database.commentDao().getCommentByCommentId(it.commentId)
                point = fetchedPoint
                comments = fetchedComments
            }
        }
    }

    CommentAndHistoryCardScreen(point = point, onChangeScreen = { state = it }, content = {
        when (state) {
            Screen.Comment -> CommentCard(comments)
            Screen.History -> HistoryCard()
        }
    }, comment = {}, refresh = {})
}

@Composable
private fun CommentAndHistoryCardScreen(
    point: EasyPoint?,
    onChangeScreen: (Screen) -> Unit,
    content: @Composable () -> Unit,
    comment: () -> Unit,
    refresh: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PointInfo(point)
        Spacer(modifier = Modifier.height(16.dp))
        Select { onChangeScreen(it) }
        content()
        BottomActionBar(refresh = { }, comment = {})
    }
}

@Composable
private fun PointInfo(points: EasyPoint?) {
    if (points != null) {
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
                    painter = rememberAsyncImagePainter(model = points.photo),
                    contentDescription = null
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "设施类别:${points.type}", fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
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
    } else Box(Modifier.fillMaxWidth()) {
        Text("点位不在数据库中")
    }
}

@Composable
private fun Select(onClick: (Screen) -> Unit) {
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
private fun CommentCard(comments: List<Comment>) {
    if (comments.isEmpty()) {
        Text("暂无")
    } else LazyColumn {
        items(comments) {
            CommentItem(it)
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    var user: User? by remember { mutableStateOf(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(user) {
        scope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context)
            val _user = database.userDao().getUserById(comment.userId)
            user = _user
        }
    }
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
        }
    }
}

@Composable
private fun HistoryCard() {
    Text("历史卡片内容,目前仍在开发")
}

@Composable
private fun BottomActionBar(refresh: () -> Unit, comment: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { refresh() }, modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(text = "更新内容")
        }

        Spacer(modifier = Modifier.width(16.dp))

        OutlinedButton(
            onClick = { comment() }, modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(text = "发布评论")
        }
    }
}

private sealed interface Screen {
    data object Comment : Screen
    data object History : Screen
}