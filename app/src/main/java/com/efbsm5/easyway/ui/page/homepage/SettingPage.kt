package com.efbsm5.easyway.ui.page.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        TopBar(title = "设置")
        Spacer(modifier = Modifier.height(8.dp))
        SettingGroup(
            items = listOf(
                SettingItem(Icons.Default.Apps, "通用设置"),
                SettingItem(Icons.Default.Notifications, "通知设置")
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingGroup(
            items = listOf(
                SettingItem(Icons.Default.Security, "账号安全"),
                SettingItem(Icons.Default.Info, "免责声明"),
                SettingItem(Icons.Default.Help, "关于我们")
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        SettingGroup(
            items = listOf(
                SettingItem(
                    Icons.AutoMirrored.Filled.ExitToApp, "注销账号", Color(0xFF1E88E5)
                ) // 蓝色图标
            )
        )
    }
}

@Composable
fun TopBar(title: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp)
            )
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun SettingGroup(items: List<SettingItem>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        items.forEachIndexed { index, item ->
            SettingRow(item)
            if (index != items.lastIndex) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE0E0E0)) // 分割线颜色
                )
            }
        }
    }
}

@Composable
fun SettingRow(item: SettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = item.iconTint,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = item.title,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Arrow",
            tint = Color(0xFFBDBDBD),
            modifier = Modifier.size(24.dp)
        )
    }
}

data class SettingItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val iconTint: Color = Color(0xFF1E88E5) // 默认蓝色
)

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen()
}