package com.efbsm5.easyway.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.map.MapController
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.ui.components.AddAndLocateButton
import com.efbsm5.easyway.ui.components.DraggableBox
import com.efbsm5.easyway.ui.components.MapPageCard
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.Screen

@Composable
fun MapPage(viewModel: MapPageViewModel) {
    val context = LocalContext.current
    val mapView = MapView(context, AMapOptions().compassEnabled(true))
    val mapController = MapController(onPoiClick = {}, onMapClick = {}, onMarkerClick = {})
    mapController.mapLocationInit(context)
    mapController.MapLifecycle(context, mapView)
    viewModel.fetchPoints(mapView)
    val content by viewModel.content.collectAsState()
    val boxHeight by viewModel.boxHeight.collectAsState()
    val location by viewModel.location.collectAsState()
    MapScreen(mapView = mapView,
        content = content,
        boxHeight = boxHeight,
        onChangeScreen = { viewModel.changeScreen(it) },
        location = location,
        onLocate = {
            mapController.moveToLocation(mapView)
        },
        onChangeHeight = { viewModel.changeBoxHeight(it) },
        navigate = { latLng, isNavigate ->
            if (isNavigate) {
                mapController.navigate(latLng, context, mapView)
            } else {
                MapUtil.onNavigate(context, latLng)
            }
        })
}

@Composable
private fun MapScreen(
    mapView: MapView,
    content: Screen,
    boxHeight: Dp,
    onChangeScreen: (Screen) -> Unit,
    location: LatLng,
    onLocate: () -> Unit,
    onChangeHeight: (Dp) -> Unit,
    navigate: (LatLng, Boolean) -> Unit,
) {
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = boxHeight),
        factory = { mapView })
    AddAndLocateButton(onAdd = {
        onChangeScreen(
            Screen.NewPoint(
                location = location
            )
        )
    }, onLocate = { onLocate() }, bottomHeight = boxHeight
    )

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        DraggableBox(
            boxHeight = boxHeight,
            onChangeHeight = { onChangeHeight(it) },
            content = {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    MapPageCard(
                        content = content,
                        onChangeScreen = { onChangeScreen(it) },
                        location = location,
                        onNavigate = navigate
                    )
                }
            },
        )
    }
    BackHandler(enabled = content != Screen.IconCard, onBack = { onChangeScreen(Screen.IconCard) })

}