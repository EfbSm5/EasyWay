package com.efbsm5.easyway.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.map.MapUtil.formatDistance
import com.efbsm5.easyway.map.MapUtil.onNavigate


@Composable
fun NewPlaceCard(latLng: LatLng, text: String) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
//    NewPlaceCardScreen(
//        selectedTab = selectedTab,
//        onChangeSelected = { selectedTab = it },
//        location = latLng,
//        context = context,
//        pois = pois,
//        easyPoints = easyPoints
//    )

}

@Composable
fun NewPlaceCardScreen(
    pois: ArrayList<PoiItemV2>?,
    easyPoints: ArrayList<EasyPoint>?,
    location: LatLng,
    selectedTab: Int,
    context: Context,
    onChangeSelected: (Int) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Tabs(titles = listOf("无障碍地点", "全部地点"),
            selectedTabIndex = selectedTab,
            onTabSelected = { onChangeSelected(it) })
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            easyPoints?.let {
                items(it) { easyPoint ->
                    AccessiblePlaceItem(
                        imageRes = easyPoint.photo.toString(),
                        title = easyPoint.name,
                        distance = MapUtil.calculateDistance(
                            location, LatLng(easyPoint.lat, easyPoint.lng)
                        ),
                        latLng = LatLng(easyPoint.lat, easyPoint.lng),
                        context = context
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            pois?.let {
                items(it) { poi ->
                    AccessiblePlaceItem(
                        imageRes = poi.photos[1].url,
                        title = poi.title,
                        distance = MapUtil.calculateDistance(
                            location, MapUtil.convertToLatLng(poi.latLonPoint)
                        ),
                        latLng = MapUtil.convertToLatLng(poi.latLonPoint),
                        context = context
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun Tabs(
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
fun AccessiblePlaceItem(
    imageRes: String, title: String, distance: Float, latLng: LatLng, context: Context
) {
    Card(shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onNavigate(
                    context = context, latLng = latLng
                )
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
                onClick = { onNavigate(context = context, latLng = latLng) },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier.size(40.dp)
            ) {
                Text("路线", fontSize = 12.sp, color = Color.Blue)
            }
        }
    }
}

