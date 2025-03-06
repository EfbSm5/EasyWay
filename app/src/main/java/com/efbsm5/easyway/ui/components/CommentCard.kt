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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.R
import com.efbsm5.easyway.data.models.EasyPoint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.data.models.assistModel.CommentAndUser
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentAndHistoryCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentCardScreen


@Composable
fun CommentAndHistoryCard(
    viewModel: CommentAndHistoryCardViewModel, navigate: (LatLng) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val pointComment by viewModel.pointComments.collectAsState()
    val showComment by viewModel.showComment.collectAsState()
    val newComment by viewModel.newComment.collectAsState()
    val point by viewModel.point.collectAsState()
    val context = LocalContext.current
    CommentAndHistoryCardScreen(
        point = point,
        onSelect = { viewModel.changeState(it) },
        state = state,
        pointComments = pointComment,
        showComment = showComment,
        newComment = newComment,
        onChangeComment = { viewModel.editComment(it) },
        changeShowComment = { viewModel.showComment(it) },
        publish = { viewModel.publish() },
        refresh = { viewModel.refresh() },
        navigate = {
            if (point != null) {
                navigate(LatLng(point!!.lat, point!!.lng))
            } else {
                MapUtil.showMsg("出错了", context = context)
            }
        })
}

@Composable
private fun CommentAndHistoryCardScreen(
    point: EasyPoint?,
    onSelect: (CommentCardScreen) -> Unit,
    state: CommentCardScreen,
    pointComments: List<CommentAndUser>?,
    showComment: Boolean,
    newComment: String,
    onChangeComment: (String) -> Unit,
    changeShowComment: (Boolean) -> Unit,
    publish: () -> Unit,
    refresh: () -> Unit,
    navigate: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PointInfo(point)
        Spacer(modifier = Modifier.height(16.dp))
        Select { onSelect(it) }
        when (state) {
            CommentCardScreen.Comment -> CommentCard(pointComments)
            CommentCardScreen.History -> HistoryCard()
        }
        if (showComment) {
            ShowTextField(text = newComment, changeText = {
                onChangeComment(it)
            }, publish = {
                changeShowComment(false)
                publish()
            }, cancel = { changeShowComment(false) })
        }
        BottomActionBar(refresh = { refresh() }, comment = {
            changeShowComment(true)
        }, navigate = { navigate() })
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
private fun Select(onClick: (CommentCardScreen) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "评论次数",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                onClick(CommentCardScreen.Comment)
            })
        Spacer(Modifier.width(50.dp))
        Text(
            text = "历史版本",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                onClick(CommentCardScreen.History)
            })
    }
}

@Composable
private fun CommentCard(comments: List<CommentAndUser>?) {
    if (comments.isNullOrEmpty()) {
        Text("暂无")
    } else LazyColumn {
        items(comments) {
            CommentItem(
                commentAndUser = it
            )
        }
    }
}

@Composable
private fun CommentItem(commentAndUser: CommentAndUser) {
    val user = commentAndUser.user
    val comment = commentAndUser.comment
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.avatar ?: R.drawable.nouser),
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = user.name, style = MaterialTheme.typography.bodyMedium
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
                    text = comment.date,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Green
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = comment.like.toString(), style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = comment.dislike.toString(), style = MaterialTheme.typography.bodySmall
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
private fun BottomActionBar(refresh: () -> Unit, comment: () -> Unit, navigate: () -> Unit) {
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
        Button(onClick = { navigate() }) {
            Text(text = "导航")
        }
    }
}

@Composable
private fun ShowTextField(
    text: String, changeText: (String) -> Unit, publish: () -> Unit, cancel: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(), value = text, onValueChange = { changeText(it) })
        Row(modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { publish() }, modifier = Modifier.weight(1f)) { Text("发布") }
            Spacer(modifier = Modifier.width(20.dp))
            Button(onClick = { cancel() }, modifier = Modifier.weight(1f)) { Text("取消") }
        }
    }

}