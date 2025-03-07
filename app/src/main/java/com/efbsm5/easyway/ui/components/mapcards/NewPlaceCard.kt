package com.efbsm5.easyway.ui.components.mapcards

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.models.assistModel.EasyPointSimplify
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.map.MapUtil.convertToLatLng
import com.efbsm5.easyway.map.MapUtil.formatDistance
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPlaceCardViewModel


@Composable
fun NewPlaceCard(
    onNavigate: (LatLng, Boolean) -> Unit, viewModel: NewPlaceCardViewModel
) {
    val selectedTabIndex by viewModel.selectedTab.collectAsState()
    val points by viewModel.points.collectAsState()
    val location = viewModel.latLng
    val showDialog by viewModel.showDialog.collectAsState()
    val poiList by viewModel.poiList.collectAsState()
    val destination = viewModel.destination
    NewPlaceCardScreen(selectedTabIndex = selectedTabIndex,
        changeTab = { viewModel.changeTab(it) },
        points = points,
        location = location!!,
        showDialog = { viewModel.showDialog(it) },
        poiList = poiList,
        ifShowDialog = showDialog,
        confirm = {
            viewModel.confirmDialog()
            onNavigate(destination!!, true)
        },
        cancel = {
            viewModel.cancelDialog()
            onNavigate(destination!!, false)
        })

}

@Composable
private fun NewPlaceCardScreen(
    selectedTabIndex: Int,
    changeTab: (Int) -> Unit,
    points: List<EasyPointSimplify>,
    location: LatLng,
    showDialog: (LatLng) -> Unit,
    poiList: List<PoiItemV2>,
    ifShowDialog: Boolean,
    confirm: () -> Unit,
    cancel: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Tabs(titles = listOf("无障碍地点", "全部地点"),
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { changeTab(it) })
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(points) { easyPoint ->
                AccessiblePlaceItem(imageRes = Uri.EMPTY,
                    title = easyPoint.name,
                    distance = easyPoint.getLatlng().let {
                        MapUtil.calculateDistance(
                            location, it
                        )
                    },
                    navigate = { showDialog(easyPoint.getLatlng()) })
                Spacer(modifier = Modifier.height(8.dp))
            }
            items(poiList) { poi ->
                AccessiblePlaceItem(imageRes = poi.photos.first().url.toUri(),
                    title = poi.title,
                    distance = MapUtil.calculateDistance(
                        location, convertToLatLng(poi.latLonPoint)
                    ),
                    navigate = { showDialog(convertToLatLng(poi.latLonPoint)) })
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    ShowDialog(
        showDialog = ifShowDialog,
        onConfirm = {
            confirm()
        },
        onCancel = { cancel() },
    )
}

@Composable
private fun Tabs(
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
private fun AccessiblePlaceItem(
    imageRes: Uri?, title: String, distance: Float, navigate: () -> Unit
) {
    Card(shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navigate()
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
private fun ShowDialog(showDialog: Boolean, onConfirm: () -> Unit, onCancel: () -> Unit) {
    if (showDialog) {
        AlertDialog(onDismissRequest = { onCancel() },
            title = { Text(text = "注意") },
            text = { Text("提供两种导航方式,确认则使用本软件进行导航,否则使用手机自带的app进行导航") },
            confirmButton = {
                TextButton(onClick = { onConfirm() }) {
                    Text("确认")
                }
            },
            dismissButton = {
                TextButton(onClick = { onCancel() }) {
                    Text("取消")
                }
            })
    }
}