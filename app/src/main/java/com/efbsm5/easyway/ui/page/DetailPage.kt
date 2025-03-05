package com.efbsm5.easyway.ui.page

import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.data.models.assistModel.CommentAndUser
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.viewmodel.pageViewmodel.DetailPageViewModel


@Composable
fun DetailPage(onBack: () -> Unit, viewModel: DetailPageViewModel) {
    val newCommentText by viewModel.newCommentText.collectAsState()
    val showTextField by viewModel.showTextField.collectAsState()
    val postUser by viewModel.postUser.collectAsState()
    val commentAndUser by viewModel.commentAndUser.collectAsState()
    val photos by viewModel.photos.collectAsState()
    val post by viewModel.post.collectAsState()
    DetailPageScreen(
        newCommentText = newCommentText,
        onAddComment = { viewModel.changeText(it) },
        changeIfShowTextField = { viewModel.ifShowTextField(it) },
        showTextField = showTextField,
        postUser = postUser,
        onBack = { onBack() },
        commentAndUser = commentAndUser,
        onLikeComment = { viewModel.addLike(it) },
        photos = photos,
        post = post
    )
}

@Composable
private fun DetailPageScreen(
    onBack: () -> Unit = {},
    post: DynamicPost,
    newCommentText: String = "",
    showTextField: Boolean = true,
    onAddComment: (String) -> Unit = {},
    changeIfShowTextField: (Boolean) -> Unit = {},
    postUser: User,
    commentAndUser: List<CommentAndUser> = emptyList(),
    onLikeComment: (Int) -> Unit = {},
    photos: List<Uri> = emptyList(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopBar { onBack() }
        Spacer(modifier = Modifier.height(16.dp))
        DetailsContent(
            post = post, user = postUser, photos = photos
        )
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        Comments(list = commentAndUser, onLike = { onLikeComment(it) })
        HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        CommentSection(comment = { changeIfShowTextField(true) })
        if (showTextField) {
            AddCommentField(commentText = newCommentText,
                onAddComment = { onAddComment(it) },
                onClickButton = { changeIfShowTextField(false) })
        }
    }
    BackHandler(enabled = showTextField) {
        changeIfShowTextField(false)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(back: () -> Unit) {
    TopAppBar(title = { Text("详情页") }, navigationIcon = {
        IconButton(onClick = { back() }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    })
}

@Composable
private fun DetailsContent(post: DynamicPost, user: User, photos: List<Uri>) {
    Row(
        modifier = Modifier.padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.avatar),
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(user.name, fontWeight = FontWeight.Bold)
            Text(post.date, color = Color.Gray)
        }
    }
    Text(post.content)
    Spacer(modifier = Modifier.height(8.dp))
    if (photos.isNotEmpty()) {
        Image(
            painter = rememberAsyncImagePainter(photos.first()),
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
private fun Comments(list: List<CommentAndUser>, onLike: (Int) -> Unit) {
    LazyColumn(modifier = Modifier.padding(vertical = 16.dp)) {
        items(list) { commentAndUser ->
            CommentItems(commentAndUser) {
                onLike(commentAndUser.comment.commentId)
            }
        }
    }
}


@Composable
private fun CommentItems(commentAndUser: CommentAndUser, like: () -> Unit) {
    var isLiked by remember { mutableStateOf(false) }
    val user = commentAndUser.user
    val comment = commentAndUser.comment
    Row(
        modifier = Modifier.padding(bottom = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                user.avatar
            ), contentDescription = "User Avatar", modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Row {
                Text(user.name, fontWeight = FontWeight.Bold)
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
private fun CommentSection(comment: () -> Unit) {
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
private fun AddCommentField(
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
