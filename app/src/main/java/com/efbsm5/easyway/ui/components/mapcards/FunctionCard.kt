package com.efbsm5.easyway.ui.components.mapcards

import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.R
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.map.MapUtil.calculateDistance
import com.efbsm5.easyway.map.MapUtil.convertToLatLng
import com.efbsm5.easyway.map.MapUtil.formatDistance
import com.efbsm5.easyway.map.MapUtil.getLatlng
import com.efbsm5.easyway.ui.components.TabSection
import com.efbsm5.easyway.viewmodel.componentsViewmodel.FunctionCardViewModel

@Composable
fun FunctionCard(
    location: LatLng,
    changeScreen: (Screen) -> Unit,
    viewModel: FunctionCardViewModel,
    navigate: (LatLng) -> Unit
) {
    val pointList by viewModel.points.collectAsState()
    val poiList by viewModel.poiList.collectAsState()
    val context = LocalContext.current
    FunctionCardScreen(
        onclick = { viewModel.search(it) },
        poiItemV2s = poiList,
        changeScreen = changeScreen,
        location = location,
        navigate = { isNavigate, destination ->
            if (isNavigate) navigate(destination)
            else viewModel.navigate(context, destination)
        },
        easyPoints = pointList
    )
}

@Composable
private fun FunctionCardScreen(
    onclick: (String) -> Unit = { },
    poiItemV2s: List<PoiItemV2> = emptyList(),
    easyPoints: List<EasyPoint> = emptyList(),
    changeScreen: (Screen) -> Unit,
    location: LatLng,
    navigate: (Boolean, LatLng) -> Unit
) {
    var isSearching by rememberSaveable { mutableStateOf(false) }
    var isShowDialog by rememberSaveable { mutableStateOf(false) }
    var destination: LatLng? = null
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        SearchBar(onClick = {
            onclick(it)
            isSearching = true
        })
        Spacer(Modifier.height(10.dp))
        if (isSearching) {
            SearchPart(
                poiItemV2s = poiItemV2s,
                easyPoints = easyPoints,
                onPoiItemV2Selected = { changeScreen(Screen.Comment( null, it, null)) },
                onPointSelected = { changeScreen(Screen.Comment(null, null, it)) },
                location = location,
                navigate = {
                    isShowDialog = true
                    destination = it
                })
        } else IconGrid {
            onclick(it)
            isSearching = true
        }
        if (isShowDialog) ShowDialog(
            callBack = {
                isShowDialog = if (it) {
                    navigate(true, destination!!)
                    false
                } else {
                    navigate(false, destination!!)
                    false
                }
            })
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
//            tint = MaterialTheme.colorScheme.surfaceTint
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun SearchPart(
    poiItemV2s: List<PoiItemV2>,
    onPoiItemV2Selected: (poiItem: PoiItemV2) -> Unit,
    onPointSelected: (point: EasyPoint) -> Unit,
    location: LatLng,
    easyPoints: List<EasyPoint> = emptyList(),
    navigate: (LatLng) -> Unit
) {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    LazyColumn(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            TabSection(
                tabs = listOf("无障碍地点", "全部地点"),
                onSelect = {
                    selectedTabIndex = it
                })
        }
        item { Spacer(modifier = Modifier.height(50.dp)) }
        if (selectedTabIndex == 1) {
            items(poiItemV2s) { poiItem ->
                AccessiblePlaceItem(
                    imageRes = poiItem.photos.firstOrNull()?.url?.toUri(),
                    title = poiItem.title,
                    distance = calculateDistance(
                        location, convertToLatLng(poiItem.latLonPoint)
                    ),
                    navigate = { navigate(convertToLatLng(poiItem.latLonPoint)) },
                    select = { onPoiItemV2Selected(poiItem) })
            }
        } else {
            items(easyPoints) { easyPoint ->
                AccessiblePlaceItem(
                    imageRes = easyPoint.photo,
                    title = easyPoint.name,
                    distance = calculateDistance(
                        location, easyPoint.getLatlng()
                    ),
                    navigate = { navigate(easyPoint.getLatlng()) },
                    select = { onPointSelected(easyPoint) })
            }
        }

    }
}

@Composable
private fun AccessiblePlaceItem(
    imageRes: Uri?, title: String, distance: Float, navigate: () -> Unit, select: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                select()
            }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                rememberAsyncImagePainter(imageRes),
                contentDescription = "地点图片",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)
            )
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = distance.formatDistance(), color = Color.Gray, fontSize = 14.sp
                )
            }
            Button(
                onClick = { navigate() },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.size(40.dp)
            ) {
                Text("路线", fontSize = 12.sp, color = Color.Blue)
            }
        }
    }
}

@Composable
private fun ShowDialog(callBack: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { callBack(false) },
        title = { Text(text = "注意") },
        text = { Text("提供两种导航方式,确认则使用本软件进行导航,否则使用手机自带的app进行导航") },
        confirmButton = {
            TextButton(onClick = { callBack(true) }) {
                Text("确认")
            }
        },
        dismissButton = {
            TextButton(onClick = { callBack(false) }) {
                Text("取消")
            }
        })
}