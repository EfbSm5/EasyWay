package com.efbsm5.easyway.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.efbsm5.easyway.R


@Preview
@Composable
fun previewIcon() {
    IconGrid {}
}

@Composable
fun IconGrid(onclick: (String) -> Unit) {
    val items = listOf(
        Pair(R.drawable.elevator, "无障碍电梯"),
        Pair(R.drawable.toliet, "无障碍厕所"),
        Pair(R.drawable.stop, "停车位"),
        Pair(R.drawable.bus, "公共交通"),
        Pair(R.drawable.lunyi, "轮椅租赁"),
        Pair(R.drawable.help, "爱心站点"),
        Pair(R.drawable.aed, "AED"),
        Pair(R.drawable.podao, "坡道")
    )
    Surface(color = MaterialTheme.colorScheme.surface) {
        LazyVerticalGrid(columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            content = {
                items(items.size) { index ->
                    val item = items[index]
                    IconCard(iconRes = item.first, text = item.second) { onclick(it) }
                }
            })
    }
}

@Composable
fun IconCard(iconRes: Int, text: String, onclick: (String) -> Unit) {
    Column(
        //horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onclick(text) }) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier
                .size(64.dp)
                .padding(bottom = 8.dp),
            contentScale = ContentScale.Fit
        )
        Text(
            text = text, fontSize = 14.sp, color = Color.Black
        )
    }
}