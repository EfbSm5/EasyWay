package com.efbsm5.easyway.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HomePage() {
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
        FeatureList()
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun ProfileHeader() {
    Row() {
        Box(
            Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "头像", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "XXXX用户", color = Color.Black
        )
    }
}


@Composable
fun FeatureList() {
    val features = listOf("我的标注", "版本切换", "帮助中心", "免责声明", "关于")
    Column(
        Modifier.fillMaxWidth()
    ) {
        features.forEach { feature ->
            FeatureItem(feature)
        }
    }
}

@Composable
fun FeatureItem(text: String) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Divider(
            Modifier
                .align(Alignment.TopStart)
                .padding(bottom = 2.dp)
        )
        Text(
            text = text, color = Color.Black
        )
    }
}

