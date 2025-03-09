package com.efbsm5.easyway.ui.page.communityPage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.efbsm5.easyway.R
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.assistModel.DynamicPostAndUser
import com.efbsm5.easyway.ui.components.DynamicPostList
import com.efbsm5.easyway.ui.components.TabSection
import com.efbsm5.easyway.viewmodel.pageViewmodel.ShowPageViewModel

@Composable
fun ShowPage(
    onChangeState: () -> Unit, onSelectedPost: (DynamicPost) -> Unit, viewModel: ShowPageViewModel
) {
    val postList = viewModel.posts.collectAsState().value
    ShowPageScreen(
        posts = postList,
        onChangeState = { onChangeState() },
        onSelect = { viewModel.changeTab(it) },
        titleText = "心无距离，共享每一刻",
        onClick = { onSelectedPost(it.dynamicPost) },
        search = { viewModel.search(it) })
}

@Composable
fun ShowPageScreen(
    posts: List<DynamicPostAndUser>,
    titleText: String,
    onChangeState: () -> Unit,
    onSelect: (Int) -> Unit,
    onClick: (DynamicPostAndUser) -> Unit,
    search: (String) -> Unit
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(text = titleText)
            BannerSection()
            SearchBar(search)
            TabSection(
                onSelect = { onSelect(it) }, tabs = listOf("全部", "活动", "互助", "分享")
            )
            DynamicPostList(posts = posts, onClick = { onClick(it) })
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            FloatingActionButton(
                onClick = { onChangeState() },
                modifier = Modifier.padding(bottom = 16.dp, end = 16.dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "添加")
            }
        }
    }
}

@Composable
fun TopBar(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        textAlign = TextAlign.Center
    )
}

@Composable
fun BannerSection() {
    Image(
        painter = painterResource(id = R.drawable.img),
        contentDescription = "活动横幅",
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .clip(RoundedCornerShape(8.dp))
    )
}

@Composable
fun SearchBar(search: (String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        TextField(
            value = text, onValueChange = { text = it }, modifier = Modifier.background(
                MaterialTheme.colorScheme.background, shape = RoundedCornerShape(8.dp)
            ), placeholder = { Text("搜索") })
        Spacer(modifier = Modifier.width(20.dp))
        IconButton(
            onClick = { search(text) },
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "搜索",
                modifier = Modifier.fillMaxSize()
            )
        }
    }

}



