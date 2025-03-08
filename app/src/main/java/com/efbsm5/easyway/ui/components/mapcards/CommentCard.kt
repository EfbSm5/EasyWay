package com.efbsm5.easyway.ui.components.mapcards

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.rounded.ThumbUp
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.assistModel.CommentAndUser
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.map.MapUtil.getInitPoint
import com.efbsm5.easyway.ui.page.communityPage.TabSection
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentAndHistoryCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentCardScreen


@Composable
fun CommentAndHistoryCard(
    viewModel: CommentAndHistoryCardViewModel, navigate: (LatLng) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val pointComment by viewModel.pointComments.collectAsState()
    val newComment by viewModel.newComment.collectAsState()
    val point by viewModel.point.collectAsState()
    val context = LocalContext.current
    CommentAndHistoryCardScreen(
        point = point,
        onSelect = { viewModel.changeState(it) },
        state = state,
        pointComments = pointComment,
        newComment = newComment,
        onChangeComment = { viewModel.editComment(it) },
        publish = { viewModel.publish() },
        refresh = { viewModel.refresh() },
        navigate = {
            if (point.pointId != 0) {
                navigate(LatLng(point.lat, point.lng))
            } else {
                MapUtil.showMsg("出错了", context = context)
            }
        })
}


@Composable
private fun CommentAndHistoryCardScreen(
    point: EasyPoint = getInitPoint(),
    onSelect: (Int) -> Unit = {},
    state: CommentCardScreen = CommentCardScreen.Comment,
    pointComments: List<CommentAndUser> = emptyList(),
    newComment: String = "",
    onChangeComment: (String) -> Unit = {},
    publish: () -> Unit = {},
    refresh: () -> Unit = {},
    navigate: () -> Unit = {},
    like: () -> Unit = {},
    dislike: () -> Unit = {}
) {
    var showComment by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        PointInfo(point, onNavigate = navigate, like = like, dislike = dislike)
        Spacer(modifier = Modifier.height(16.dp))
        Select(onSelect)
        when (state) {
            CommentCardScreen.Comment -> CommentCard(pointComments)
            CommentCardScreen.History -> HistoryCard()
        }
        if (showComment) {
            ShowTextField(text = newComment, changeText = onChangeComment, publish = {
                showComment = false
                publish()
            }, cancel = { showComment = false })
        }
        BottomActionBar(comment = { showComment = true })
    }
}

@Composable
fun PointInfo(easyPoint: EasyPoint, onNavigate: () -> Unit, like: () -> Unit, dislike: () -> Unit) {
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    val likeColor by animateColorAsState(targetValue = if (isLiked) Color.Red else Color.Gray)
    val dislikeColor by animateColorAsState(targetValue = if (isDisliked) Color.Red else Color.Gray)
    val likeSize by animateFloatAsState(targetValue = if (isLiked) 36f else 24f)
    val dislikeSize by animateFloatAsState(targetValue = if (isDisliked) 36f else 24f)
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.Start) {
        Image(
            painter = rememberAsyncImagePainter(easyPoint.photo),
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(easyPoint.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { onNavigate() }) {
                Text("导航")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(
                Icons.Rounded.ThumbUp,
                contentDescription = "like",
                modifier = Modifier
                    .size(likeSize.dp)
                    .clickable {
                        like()
                        isLiked = !isLiked
                        if (isDisliked) isDisliked = false
                    },
                tint = likeColor
            )
            Text(easyPoint.likes.toString(), modifier = Modifier.padding(start = 4.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Icon(
                modifier = Modifier
                    .size(dislikeSize.dp)
                    .clickable {
                        dislike()
                        isDisliked = !isDisliked
                        if (isLiked) isLiked = false
                    },
                painter = painterResource(id = R.drawable.thumb_down),
                contentDescription = "Dislike",
                tint = dislikeColor
            )
            Text(easyPoint.dislikes.toString(), modifier = Modifier.padding(start = 4.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("详细地址", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(easyPoint.location, fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(
                painter = painterResource(id = R.drawable.clock), contentDescription = "Update Time"
            )
            Text("更新日期: ${easyPoint.refreshTime}", modifier = Modifier.padding(start = 4.dp))
        }
        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
            Icon(
                Icons.Default.Person, contentDescription = "Source"
            )
            Text("点位来源:${easyPoint.userId}", modifier = Modifier.padding(start = 4.dp))
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("历史版本")
        }
    }
}

@Composable
private fun Select(onClick: (Int) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    TabSection(
        selectedTabIndex = selectedIndex,
        tabs = listOf("评论", "历史"),
        onSelect = {
            selectedIndex = it
            it.let(
                onClick
            )
        },
    )
}

@Composable
private fun CommentCard(comments: List<CommentAndUser>) {
    if (comments.isEmpty()) {
        Text("暂无")
    } else LazyColumn {
        items(comments) {
            CommentItem(
                commentAndUser = it
            )
        }
    }
}

@Preview
@Composable
fun a() {
//    CommentItem(
//        commentAndUser = CommentAndUser(
//            comment = Comment(
//                index = 1,
//                commentId = 1,
//                userId = 1,
//                content = "这是一条评论",
//                like = 1,
//                dislike = 1,
//                date = "2025.3.8"
//            ), user = MapUtil.getInitUser()
//        )
//    )
    BottomActionBar(comment = {})
}

@Composable
private fun CommentItem(commentAndUser: CommentAndUser) {
    val user = commentAndUser.user
    val comment = commentAndUser.comment
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.avatar ?: R.drawable.nouser),
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentDescription = "avatar"
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
private fun BottomActionBar(comment: () -> Unit) {
    Box(contentAlignment = Alignment.BottomEnd) {
        OutlinedButton(
            onClick = { comment() }, modifier = Modifier.height(48.dp)

        ) {
            Text(text = "发布评论")
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