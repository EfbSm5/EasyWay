package com.efbsm5.easyway.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.efbsm5.easyway.R


@Composable
fun NewPlaceCard() {
    AccessiblePlacesScreen()
}

@Composable
fun AccessiblePlacesScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    Column(modifier = Modifier.fillMaxSize()) {
        Tabs(titles = listOf("无障碍地点", "全部地点"),
            selectedTabIndex = selectedTab,
            onTabSelected = { selectedTab = it })

        // 列表内容
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(5) { index ->
                AccessiblePlaceItem(imageRes = R.drawable.aed, // 替换为实际的图片资源
                    title = "地点标题 $index",
                    distance = "直线${(0.3 + index * 0.4).format(2)}km",
                    onNavigateClick = { /* 路线按钮点击事件 */ })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun Tabs(
    titles: List<String>, selectedTabIndex: Int, onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        titles.forEachIndexed { index, title ->
            Text(text = title,
                color = if (index == selectedTabIndex) Color.Blue else Color.Gray,
                fontWeight = if (index == selectedTabIndex) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier
                    .clickable { onTabSelected(index) }
                    .padding(8.dp))
        }
    }
}

@Composable
fun AccessiblePlaceItem(
    imageRes: Int, title: String, distance: String, onNavigateClick: () -> Unit
) {
    Card(shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* 点击列表项的事件 */ }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 左侧图片
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "地点图片",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)
            )

            // 中间文本内容
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = distance, color = Color.Gray, fontSize = 14.sp
                )
            }

            // 右侧按钮
            Button(
                onClick = onNavigateClick,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.size(40.dp)
            ) {
                Text("路线", fontSize = 12.sp, color = Color.Blue)
            }
        }
    }
}

// 扩展函数用于格式化小数
fun Double.format(digits: Int) = "%.${digits}f".format(this)