package com.efbsm5.easyway.components

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewPointCard() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "新增标识",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            DropdownField(label = "设施类别")
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithText(label = "设施说明", text = "") {}
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithText(label = "所在位置", text = "") {}
            Spacer(modifier = Modifier.height(16.dp))
            UploadImageSection {}
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { /* 确认上传逻辑 */ },
                colors = ButtonDefaults.buttonColors(contentColor = Color.Green)
            ) {
                Text(text = "确认上传")
            }
            Button(
                onClick = { /* 取消逻辑 */ },
                colors = ButtonDefaults.buttonColors(contentColor = Color.Gray)
            ) {
                Text(text = "取消")
            }
        }
    }
}

@Composable
fun DropdownField(label: String) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    Column {
        Text(text = label, style = TextStyle(fontSize = 16.sp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .padding(8.dp)
            .clickable { expanded = true }) {
            Text(text = if (selectedOption.isEmpty()) "请选择" else selectedOption)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            listOf(
                "无障碍电梯",
                "无障碍厕所",
                "停车位",
                "公共交通",
                "轮椅租赁",
                "爱心站点",
                "AED",
                "坡道"
            ).forEach { option ->
                DropdownMenuItem(onClick = {
                    selectedOption = option
                    expanded = false
                }, text = { Text(option) })
            }
        }
    }
}

@Composable
fun TextFieldWithText(label: String, text: String, onValueChange: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, style = TextStyle(fontSize = 16.sp))
        TextField(value = text, onValueChange = { onValueChange(it) })
    }
}

@Composable
fun UploadImageSection(onChoosePicture: (Uri?) -> Unit) {
    Column {
        Text(text = "上传图片", style = TextStyle(fontSize = 16.sp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.onBackground)
                .border(1.dp, MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "严禁上传无关图片", color = Color.Red, fontSize = 12.sp)
        }
    }
}

@Composable
fun OnChoosePicture(callback: (Uri?) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            callback(result.data?.data)
        }
    }
    LaunchedEffect(Unit) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "picture/*"
        }
        launcher.launch(intent)
    }
}


@Composable
fun SearchPage() {
    var offsetY by remember { mutableStateOf(200f) }
    val animatedOffsetY by animateDpAsState(targetValue = offsetY.dp)
    val iconRowOffsetY by animateDpAsState(
        targetValue = if (offsetY < 150f) 0.dp else 200.dp // 上拉时从 200.dp 滑动到 0.dp
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    offsetY = (offsetY + dragAmount).coerceIn(0f, 200f)
                }
            }
    ) {
        // 地图内容
        MapContent()
        Column(
            Modifier
                .offset(y = animatedOffsetY) // 根据偏移量更新位置
                .fillMaxWidth()
                .background(Color.White)
        ) {
            SearchBar()
            IconRow(
                Modifier
                    .offset(y = iconRowOffsetY) // 控制 IconRow 的垂直位置
            )
        }
    }
}

@Composable
fun MapContent() {
    // 地图模拟内容
    Box(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFE0F7FA)) // 蓝色背景代表地图
    ) {
        Text(
            text = "地图内容",
            Modifier.align(Alignment.Center),
            color = Color.Black
        )
    }
}

@Composable
fun SearchBar() {
    // 搜索栏组件
    Box(
        Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "搜索栏", color = Color.White)
    }
}

@Composable
fun IconRow(modifier: Modifier = Modifier) {
    // 图标区域组件
    Row(
        modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        repeat(4) {
            Box(
                Modifier
                    .size(50.dp)
                    .background(Color.DarkGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "图标", color = Color.White)
            }
        }
    }
}
