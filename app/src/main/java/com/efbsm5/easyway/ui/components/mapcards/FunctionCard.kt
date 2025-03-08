package com.efbsm5.easyway.ui.components.mapcards

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.R
import com.efbsm5.easyway.map.MapUtil.calculateDistance
import com.efbsm5.easyway.map.MapUtil.convertToLatLng
import com.efbsm5.easyway.viewmodel.componentsViewmodel.FunctionCardViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.Screen

@Composable
fun FunctionCard(
    location: LatLng, changeScreen: (Screen) -> Unit, viewModel: FunctionCardViewModel
) {
    val poiList by viewModel.poiList.collectAsState()
    FunctionCardScreen(
        onclick = { viewModel.searchForPoi(it) },
        poiItemV2s = poiList,
        onSelected = {
            changeScreen(
                Screen.Comment(
                    marker = null, poi = null, poiItemV2 = it
                )
            )
        },
        location = location
    )
}

@Composable
private fun FunctionCardScreen(
    onclick: (String) -> Unit = { },
    poiItemV2s: List<PoiItemV2>,
    onSelected: (poiItem: PoiItemV2) -> Unit,
    location: LatLng
) {
    var isSearching by rememberSaveable { mutableStateOf(false) }
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SearchBar(onClick = onclick)
        Spacer(Modifier.height(10.dp))
        if (isSearching) SearchPart(
            poiItemV2s = poiItemV2s, onSelected = onSelected, location = location
        )
        else IconGrid {
            onclick(it)
            isSearching = true
        }
    }
}

@Composable
private fun SearchBar(onClick: (String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("搜索") },
            modifier = Modifier.clip(RoundedCornerShape(9))
        )
        IconButton(onClick = { onClick(text) }) {
            Icon(
                Icons.Default.Search, contentDescription = "search"
            )
        }
    }
}

@Composable
private fun IconGrid(onclick: (String) -> Unit) {
    val items = listOf(
        Pair(R.drawable.car, "汽车"),
        Pair(R.drawable.heart, "爱心站点"),
        Pair(R.drawable.rest, "娱乐设施"),
        Pair(R.drawable.park, "停车位"),
        Pair(R.drawable.lift, "电梯"),
        Pair(R.drawable.toliet, "厕所"),
        Pair(R.drawable.podao, "坡道"),
        Pair(R.drawable.lunyi, "轮椅租赁"),
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(4), modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), content = {
            items(items.size) { index ->
                val item = items[index]
                IconAndName(iconRes = item.first, text = item.second) { onclick(item.second) }
            }
        })
}

@Composable
private fun IconAndName(iconRes: Int, text: String, onclick: () -> Unit) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(19))
            .clickable { onclick() }) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier
                .size(50.dp)
                .padding(bottom = 8.dp),
            tint = MaterialTheme.colorScheme.surfaceTint
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun SearchPart(
    poiItemV2s: List<PoiItemV2>,
    onSelected: (poiItem: PoiItemV2) -> Unit,
    location: LatLng,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item { Spacer(modifier = Modifier.height(50.dp)) }
        items(poiItemV2s) { poiItem ->
            PoiItemPreview(
                poiItem = poiItem, location = location, select = { onSelected(poiItem) })
        }
    }
}

@Composable
fun PoiItemPreview(poiItem: PoiItemV2, location: LatLng, select: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                rememberAsyncImagePainter(poiItem.photos.firstOrNull()),
                contentDescription = "photo",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = poiItem.title, fontSize = 18.sp)
                Text(
                    text = "直线${
                        calculateDistance(
                            convertToLatLng(poiItem.latLonPoint), location
                        )
                    }", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Button(onClick = select) {
                Text("路线")
            }
        }
    }
}