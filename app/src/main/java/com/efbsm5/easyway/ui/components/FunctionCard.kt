package com.efbsm5.easyway.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun FunctionCard(onclick: (String) -> Unit, onChangeSearchPage: () -> Unit) {
    var text by remember { mutableStateOf("") }
    FunctionCardScreen(
        text = text,
        onTextChange = { text = it },
        onclick = { onclick(it) },
        onClickSearchBar = { onChangeSearchPage() })
}

@Composable
private fun FunctionCardScreen(
    text: String = "",
    onTextChange: (String) -> Unit = {},
    onclick: (String) -> Unit = { },
    onClickSearchBar: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SearchBar(
            text = text,
            onTextChange = { onTextChange(it) },
            onClickSearchBar = { onClickSearchBar() })
        Spacer(Modifier.height(10.dp))
        IconGrid { onclick(it) }
    }
}

@Composable
private fun SearchBar(text: String, onTextChange: (String) -> Unit, onClickSearchBar: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable {
                onClickSearchBar()
            }) {
        TextField(
            value = text,
            onValueChange = { onTextChange(it) },
            label = { Text("搜索") },
            modifier =Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Search, "search") },
        )
        Button(onClick = { onClickSearchBar() }) {
            Text("搜索")
        }
    }

}


@Composable
private fun IconGrid(onclick: (String) -> Unit) {
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
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
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
    Column(
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

