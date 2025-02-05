package com.efbsm5.easyway.ui.page

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.efbsm5.easyway.map.MapUtil

@Composable
fun HomePage() {
    val context = LocalContext.current
    HomePageScreen(context, onUpdate = {

    })
}

@Composable
private fun HomePageScreen(context: Context, onUpdate: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        ProfileHeader()
        Spacer(modifier = Modifier.height(32.dp))
        MenuItem("我的标注") { MapUtil.showMsg("开发中", context) }
        MenuItem("版本切换") { MapUtil.showMsg("开发中", context) }
        MenuItem("帮助中心") { MapUtil.showMsg("开发中", context) }
        MenuItem("免责声明") { MapUtil.showMsg("开发中", context) }
        MenuItem("关于") { MapUtil.showMsg("开发中", context) }
        MenuItem("同步数据") { onUpdate() }
    }
}

@Composable
private fun ProfileHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "头像", color = Color.White)
        }
        Spacer(modifier = Modifier.width(40.dp))
        Text(
            text = "XXL用户", color = Color.Black
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




