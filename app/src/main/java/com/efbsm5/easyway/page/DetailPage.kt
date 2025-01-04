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
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.R
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.User
import com.efbsm5.easyway.database.AddLikeForComment
import com.efbsm5.easyway.database.AppDataBase
import com.efbsm5.easyway.database.getCommentByCommentId
import com.efbsm5.easyway.database.getUserByUserId
import com.efbsm5.easyway.map.MapUtil


@Composable
fun DetailPage(post: DynamicPost) {
    val context = LocalContext.current
    var newCommentText by remember { mutableStateOf("") }
    var showTextField by remember { mutableStateOf(false) }
    val comments = getCommentByCommentId(context, post.commentId)
    val user = getUserByUserId(context, post.userId)
    DetailPageScreen(
        post = post,
        newCommentText = newCommentText,
        onAddComment = { newCommentText = it },
        changeIfShowTextField = { showTextField = it },
        showTextField = showTextField,
        user = user,
        comments = comments
    )
}

@Composable
fun DetailPageScreen(
    post: DynamicPost,
    newCommentText: String,
    showTextField: Boolean,
    onAddComment: (String) -> Unit,
    changeIfShowTextField: (Boolean) -> Unit,
    user: User?,
    comments: List<Comment>?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar()
        Spacer(modifier = Modifier.height(16.dp))
        DetailsContent(
            post = post, user = user
        )
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        Comments(comments = comments)
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        CommentSection(comment = { changeIfShowTextField(true) })
        if (showTextField) {
            AddCommentField(commentText = newCommentText,
                onAddComment = { onAddComment(it) },
                onClickButton = { changeIfShowTextField(false) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(title = { Text("详情页") }, navigationIcon = {
        IconButton(onClick = { /* 返回逻辑 */ }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    })
}

@Composable
fun DetailsContent(post: DynamicPost, user: User?) {
    Row(
        modifier = Modifier.padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(user?.avatar ?: R.drawable.nouser),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(user?.name ?: "用户不存在", fontWeight = FontWeight.Bold)
            Text(post.date, color = Color.Gray)
        }
    }
    Text(post.content)
    Spacer(modifier = Modifier.height(8.dp))
    if (post.photos.isNotEmpty()) {
        Image(
            painter = rememberAsyncImagePainter(post.photos.first().url),
            contentDescription = "Post Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp))
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        Icon(Icons.Default.ThumbUp, contentDescription = "Like")
        Spacer(modifier = Modifier.width(8.dp))
        Text(post.like.toString())
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
fun Comments(comments: List<Comment>?) {
    val context = LocalContext.current
    if (!comments.isNullOrEmpty()) {
        LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
            items(comments) { comment ->
                CommentItems(comment) {
                    AppDataBase.getDatabase(context).commentDao().incrementLikes(comment.index)
                }
            }
        }
    }
}

@Composable
fun CommentItems(comment: Comment, like: () -> Unit) {
    var isLiked by remember { mutableStateOf(false) }
    val user = getUserByUserId(LocalContext.current, comment.userId)
    Row(
        modifier = Modifier.padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                user?.avatar ?: R.drawable.nouser
            ), contentDescription = "User Avatar", modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row {
                Text(user?.name ?: "用户不存在", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Text(comment.date, color = Color.Gray)
            }
            Text(comment.content)
        }
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            Icons.Default.ThumbUp, contentDescription = "Like", modifier = Modifier.clickable {
                isLiked = true
                comment.like += 1
                like()
            }, tint = if (isLiked) Color.Red else Color.Black
        )
    }
}

@Composable
fun CommentSection(comment: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Row(
            modifier = Modifier
                .height(30.dp)
                .width(80.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable { comment() }, verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Edit, contentDescription = "Write Comment")
            Spacer(modifier = Modifier.width(8.dp))
            Text("写回复", color = Color.Gray)
        }
    }
}

@Composable
fun AddCommentField(
    commentText: String, onAddComment: (String) -> Unit, onClickButton: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TextField(value = commentText,
            onValueChange = { onAddComment(it) },
            modifier = Modifier.weight(1f),
            placeholder = { Text("添加评论") })
        Spacer(modifier = Modifier.width(8.dp))
        Button(onClick = {
            onClickButton()
        }) {
            Text("发送")
        }
    }
}