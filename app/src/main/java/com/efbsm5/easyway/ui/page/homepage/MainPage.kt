package com.efbsm5.easyway.ui.page.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageState

@Composable
fun HomePageMain(onUpdate: () -> Unit, user: User, changeState: (HomePageState) -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        ProfileHeader(user)
        Spacer(modifier = Modifier.height(32.dp))
        MenuItem("我的标注") { changeState(HomePageState.Point) }
        MenuItem("我的动态") { changeState(HomePageState.Post) }
        MenuItem("我的评论") { changeState(HomePageState.Comment) }
        MenuItem("版本切换") { }
        MenuItem("帮助中心") { }
        MenuItem("免责声明") { }
        MenuItem("关于") { }
        MenuItem("同步数据") { onUpdate() }
    }
}

@Composable
private fun ProfileHeader(user: User) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(user.avatar),
            "头像",
            Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(40.dp))
        Text(
            text = user.name, color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun MenuItem(title: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = { onClick() }),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}
