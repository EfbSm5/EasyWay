package com.efbsm5.easyway.page

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.efbsm5.easyway.components.AddAndLocateButton
import com.efbsm5.easyway.components.CommentAndHistoryCard
import com.efbsm5.easyway.components.DraggableBox
import com.efbsm5.easyway.components.FunctionCard
import com.efbsm5.easyway.components.NewPlaceCard
import com.efbsm5.easyway.components.NewPointCard
import com.efbsm5.easyway.database.AppDataBase
import com.efbsm5.easyway.map.MapController
import com.efbsm5.easyway.map.MapSaver.isMapControllerInitialized
import com.efbsm5.easyway.map.MapSaver.isMapViewInitialized
import com.efbsm5.easyway.map.MapSaver.isPointsInitialized
import com.efbsm5.easyway.map.MapSaver.mapController
import com.efbsm5.easyway.map.MapSaver.mapView
import com.efbsm5.easyway.map.MapSaver.points
import com.efbsm5.easyway.map.MapUtil.showMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MapPage"

@Composable
fun MapPage() {
    val context = LocalContext.current
    var content: Screen by remember { mutableStateOf(Screen.IconCard) }
    InitMap(context = context) { content = it }
    var boxHeight by remember { mutableStateOf(100.dp) }
    MapScreen(
        onAdd = { content = Screen.NewPoint(mapController.getLastKnownLocation()) },
        onLocate = { mapController.onLocate() },
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.surfaceContainer)
            ) {
                when (content) {
                    is Screen.Comment -> {
                        CommentAndHistoryCard(marker = (content as Screen.Comment).marker)
                    }

                    Screen.IconCard -> FunctionCard(onclick = {
                        content = Screen.Places(it)
                    })

                    is Screen.NewPoint -> NewPointCard(
                        (content as Screen.NewPoint).location,
                        back = { content = Screen.IconCard }
                    )

                    is Screen.Places -> {
                        NewPlaceCard(
                            mapController.getLastKnownLocation()!!,
                            (content as Screen.Places).name,
                        )
                    }
                }
            }
        },
        boxHeight = boxHeight,
        onChangeHeight = { boxHeight = it },
    )
    BackHandler(enabled = content != Screen.IconCard, onBack = { content = Screen.IconCard })
}

@Composable
private fun MapScreen(
    onAdd: () -> Unit,
    onLocate: () -> Unit,
    boxHeight: Dp,
    onChangeHeight: (Dp) -> Unit,
    content: @Composable () -> Unit
) {
    DisposableEffect(Unit) {
        onDispose {
            mapView.onPause()
            Log.e(TAG, "MapScreen: on pause")
        }
    }
    AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView }, update = { view ->
//        if (view.parent != null) {
//            (view.parent as ViewGroup).removeView(view)
//        }
//        (view.parent as? ViewGroup)?.addView(view)
//        view.onResume()
    })
    AddAndLocateButton(onAdd = {
        onAdd()
    }, onLocate = { onLocate() }, bottomHeight = boxHeight
    )
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        DraggableBox(
            boxHeight = boxHeight, onChangeHeight = { onChangeHeight(it) }, content = content
        )
    }
}

@Composable
private fun InitMap(context: Context, onChange: (Screen) -> Unit) {
    if (!isMapViewInitialized()) {
        mapView = MapView(context, AMapOptions().compassEnabled(true))
        Log.e(TAG, "InitMap: initMapview")
    }
    if (!isMapControllerInitialized()) {
        mapController = MapController(
            onPoiClick = { showMsg(it!!.name, context) },
            onMapClick = { showMsg(it!!.latitude.toString(), context) },
            onMarkerClick = {
                it?.let {
                    onChange(Screen.Comment(marker = it))
                    showMsg("click ${it.title}", context)
                }
            },
        )
        mapController.InitMapLifeAndLocation(context)
        Log.e(TAG, "InitMap: initMapviewController")
    }
    if (!isPointsInitialized()) {
        val scope = rememberCoroutineScope()
        LaunchedEffect(Unit) {
            scope.launch(Dispatchers.IO) {
                val database = AppDataBase.getDatabase(context)
                val point = database.pointsDao().loadAllPoints()
                point.forEach({ EasyPoint ->
                    mapView.map.addMarker(
                        MarkerOptions().title(EasyPoint.name)
                            .position(LatLng(EasyPoint.lat, EasyPoint.lng))
                            .icon(BitmapDescriptorFactory.defaultMarker())
                    )
                })
                points.addAll(point)
            }
        }
    }
}

sealed interface Screen {
    data object IconCard : Screen
    data class NewPoint(val location: LatLng?) : Screen
    data class Places(val name: String) : Screen
    data class Comment(val marker: Marker) : Screen
}

