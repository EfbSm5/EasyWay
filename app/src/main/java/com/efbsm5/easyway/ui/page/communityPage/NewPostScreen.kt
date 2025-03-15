package com.efbsm5.easyway.ui.page.communityPage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.viewmodel.pageViewmodel.NewPostPageViewModel


@Composable
fun NewDynamicPostPage(navigate: () -> Unit, viewModel: NewPostPageViewModel) {
    val newPost by viewModel.newPost.collectAsState()
    val selectedButton by viewModel.selectedButton.collectAsState()
    val photos by viewModel.chosenPhotos.collectAsState()
    DynamicPostScreen(
        dynamicPost = newPost,
        selectedButton = selectedButton,
        onSelected = { viewModel.changeSelectedButton(it) },
        onTitleChanged = { viewModel.editPost(newPost.copy(title = it)) },
        onContentChanged = { viewModel.editPost(newPost.copy(content = it)) },
        photos = photos,
        onSelectedPhoto = {
            it?.let {
                viewModel.getPicture(it)
            }
        },
        publish = {
            viewModel.push()
            navigate()
        },
        back = { navigate() })
}

@Composable
fun DynamicPostScreen(
    dynamicPost: DynamicPost = MapUtil.getInitPost(),
    selectedButton: String = "",
    photos: List<Uri> = emptyList(),
    onSelected: (String) -> Unit = {},
    onTitleChanged: (String) -> Unit = {},
    onContentChanged: (String) -> Unit = {},
    onSelectedPhoto: (Uri?) -> Unit = {},
    publish: () -> Unit = {},
    back: () -> Unit = {}
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopBar(back = { back() })
            PublishToSection(selectedButton = selectedButton, onSelected = { onSelected(it) })
            Spacer(modifier = Modifier.height(16.dp))
            AddTitleAndContentSection(
                dynamicPost = dynamicPost,
                onTitleChanged = { onTitleChanged(it) },
                onContentChanged = { onContentChanged(it) })
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            AddLocationAndImagesSection(
                selectedPhotos = photos, onSelectedPhoto = { it?.let { onSelectedPhoto(it) } })
            Spacer(modifier = Modifier.weight(1f))
            PublishButton(publish = { publish() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(back: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = { back() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
            }
        },
        title = { Text("动态发布", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
    )
}

@Composable
private fun PublishToSection(selectedButton: String, onSelected: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("发布到：", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.width(8.dp))
        Row {
            PublishButton("活动", selectedButton) { onSelected("活动") }
            PublishButton("互助", selectedButton) { onSelected("互助") }
            PublishButton("互动", selectedButton) { onSelected("互动") }
        }
    }
}

@Composable
private fun PublishButton(label: String, selectedButton: String, onSelected: () -> Unit) {
    val isSelected = selectedButton == label
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.Blue else Color.Transparent, label = ""
    )
    Button(
        onClick = { onSelected() },
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(label, color = if (isSelected) Color.White else Color.Black)
    }
}


@Composable
private fun AddTitleAndContentSection(
    dynamicPost: DynamicPost, onTitleChanged: (String) -> Unit, onContentChanged: (String) -> Unit
) {
    Column {
        TextField(
            value = dynamicPost.title,
            onValueChange = { onTitleChanged(it) },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("添加标题") },
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = dynamicPost.content,
            onValueChange = { onContentChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            placeholder = { Text("添加正文") },
        )
    }
}

@Composable
private fun AddLocationAndImagesSection(
    selectedPhotos: List<Uri>, onSelectedPhoto: (Uri?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onSelectedPhoto(result.data?.data)
        }
    }
    var showDialog by remember { mutableStateOf<Uri?>(null) }
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /*TODO*/


            }) {
                Icon(
                    Icons.Default.LocationOn, contentDescription = "添加地点"
                )
            }
            Text("添加地点", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(4), modifier = Modifier.height(200.dp)
        ) {
            items(selectedPhotos.size) { index ->
                val photoUri = selectedPhotos[index]
                Image(
                    painter = rememberAsyncImagePainter(photoUri),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp)
                        .clickable { showDialog = photoUri })
            }
            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(MaterialTheme.colorScheme.onBackground)
                        .border(1.dp, MaterialTheme.colorScheme.primary)
                        .clickable {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "image/*"
                            }
                            launcher.launch(intent)
                        }, contentAlignment = Alignment.Center
                ) {
                    Text("+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
    if (showDialog != null) {
        AlertDialog(onDismissRequest = { showDialog = null }, text = {
            Image(
                painter = rememberAsyncImagePainter(showDialog),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxWidth()
            )
        }, confirmButton = {
            TextButton(onClick = { showDialog = null }) {
                Text("关闭")
            }
        })
    }
}

@Composable
private fun PublishButton(publish: () -> Unit) {
    Button(
        onClick = { publish() },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(24.dp),
    ) {
        Text("发布帖子", color = Color.White, fontSize = 16.sp)
    }
}