package com.efbsm5.easyway.page

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.components.AddAndLocateButton
import com.efbsm5.easyway.components.DraggableBox
import com.efbsm5.easyway.components.FunctionCard
import com.efbsm5.easyway.components.NewPointCard
import com.efbsm5.easyway.map.MapSaver.initializeMapController
import com.efbsm5.easyway.map.MapSaver.initializeMapView
import com.efbsm5.easyway.map.MapSaver.mapController
import com.efbsm5.easyway.map.MapSaver.mapView
import com.efbsm5.easyway.map.MapUtil.showMsg


@Composable
fun MapPage() {
    val context = LocalContext.current
    var content: Screen by remember { mutableStateOf(Screen.IconCard) }
    var boxHeight by remember { mutableStateOf(100.dp) }
    InitMap(context = context) { content = it }

    MapScreen(
        onAdd = { content = Screen.NewPoint },
        onLocate = { mapController.onLocate() },
        content = {
            when (content) {
                is Screen.Comment -> {
//                    CommentAndHistoryCard(marker = (content as Screen.Comment).marker)
                }

                Screen.IconCard -> FunctionCard(onclick = {
                    content = Screen.Places(it)
                })

                Screen.NewPoint -> NewPointCard(mapController.getLastKnownLocation()!!)

                is Screen.Places -> {
//                    NewPlaceCard(
//                        mapController.getLastKnownLocation()!!,
//                        (content as Screen.Places).name,
//                        easyPoints = null
//                    )
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
    AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView })
    AddAndLocateButton(onAdd = {
        onAdd()
    }, onLocate = { onLocate() })
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
    initializeMapView(context)
    initializeMapController(
        onPoiClick = { showMsg(it!!.name, context) },
        onMapClick = { showMsg(it!!.latitude.toString(), context) },
        onMarkerClick = {
            it?.let {
                onChange(Screen.Comment(marker = it))
            }
        },
    )
    mapController.MapLocationInit(context)
    mapController.MapLifecycle(context)

}

sealed interface Screen {
    data object IconCard : Screen
    data object NewPoint : Screen
    data class Places(val name: String) : Screen
    data class Comment(val marker: Marker) : Screen
}

