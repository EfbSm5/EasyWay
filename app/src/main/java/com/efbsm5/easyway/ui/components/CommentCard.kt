package com.efbsm5.easyway.ui.components

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
import com.efbsm5.easyway.data.database.AppDataBase
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.efbsm5.easyway.data.User
import com.efbsm5.easyway.viewmodel.CommentViewModel
import com.efbsm5.easyway.viewmodel.ViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun CommentAndHistoryCard(marker: Marker) {
    val context = LocalContext.current
    val commentViewModel = viewModel<CommentViewModel>(factory = ViewModelFactory(context))
    commentViewModel.getCommentFromMarker(marker, context)
    val comments = commentViewModel.comments.collectAsState()
    val point = commentViewModel.point.collectAsState()
    var state: Screen by rememberSaveable { mutableStateOf(Screen.Comment) }
    var showComment by remember { mutableStateOf(false) }
    val comment by remember { mutableStateOf(Comment()) }
    CommentAndHistoryCardScreen(point = point.value, onChangeScreen = { state = it }, content = {
        when (state) {
            Screen.Comment -> CommentCard(comments.value)
            Screen.History -> HistoryCard()
        }
        if (showComment) {
            ShowTextField(text = comment.content,
                changeText = { comment.content = it },
                publish = { showComment = false },
                cancel = { showComment = false })
        }
    }, comment = {
        showComment = true
        commentViewModel.insertComment(comment, context)
    }, refresh = {})
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
        BottomActionBar(refresh = { refresh() }, comment = { comment() })
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
private fun CommentCard(comments: List<Comment>?) {
    if (comments.isNullOrEmpty()) {
        Text("暂无")
    } else LazyColumn {
        items(comments) {
            CommentItem(it)
        }
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    var user by remember { mutableStateOf<User?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(comment.userId) {
        scope.launch(Dispatchers.IO) {
            user = AppDataBase.getDatabase(context).userDao().getUserById(comment.userId)
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
            contentDescription = stringResource(R.string.avatar)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(
                        R.string.username, user?.name ?: stringResource(R.string.user_not_found)
                    ), style = MaterialTheme.typography.bodyMedium
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
                    text = stringResource(R.string.time, comment.date),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = stringResource(R.string.like),
                    modifier = Modifier.size(16.dp),
                    tint = Color.Green
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.like, comment.like),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(R.string.dislike),
                    modifier = Modifier.size(16.dp),
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.dislike, comment.dislike),
                    style = MaterialTheme.typography.bodySmall
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

@Composable
private fun ShowTextField(
    text: String, changeText: (String) -> Unit, publish: () -> Unit, cancel: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
                value = text,
                onValueChange = { changeText(it) })
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { publish() }, modifier = Modifier.weight(1f)) { Text("发布") }
                Spacer(modifier = Modifier.width(20.dp))
                Button(onClick = { cancel() }, modifier = Modifier.weight(1f)) { Text("取消") }
            }

        }

    }
}


sealed interface Screen {
    data object Comment : Screen
    data object History : Screen
}