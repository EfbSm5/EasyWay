package com.efbsm5.easyway.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.efbsm5.easyway.R

@Composable
fun FunctionCard(text: String, onclick: (String) -> Unit, onTextChange: (String) -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .heightIn(max = 350.dp)
            .padding(16.dp)
    ) {
        SearchBar(text = text) { onTextChange(it) }
        Spacer(Modifier.height(10.dp))
        IconGrid { onclick(it) }
    }
}

@Composable
fun SearchBar(text: String, onTextChange: (String) -> Unit) {
    TextField(
        value = text,
        onValueChange = { onTextChange(it) },
        label = { Text("搜索") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        leadingIcon = { Icon(Icons.Default.Search, null) },
    )
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
    LazyVerticalGrid(columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(RoundedCornerShape(9)),
        content = {
            items(items.size) { index ->
                val item = items[index]
                IconAndName(iconRes = item.first, text = item.second) { onclick(it) }
            }
        })
}

@Composable
private fun IconAndName(iconRes: Int, text: String, onclick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.Center,
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
            contentScale = ContentScale.Fit,
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
