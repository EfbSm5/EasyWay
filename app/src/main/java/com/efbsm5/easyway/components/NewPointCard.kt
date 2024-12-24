package com.efbsm5.easyway.components

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NewPointCard() {

}

@Composable
fun FormLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
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