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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.map.MapController
import com.efbsm5.easyway.ui.components.AddAndLocateButton
import com.efbsm5.easyway.ui.components.CommentAndHistoryCard
import com.efbsm5.easyway.ui.components.DraggableBox
import com.efbsm5.easyway.ui.components.FunctionCard
import com.efbsm5.easyway.ui.components.MapPageCard
import com.efbsm5.easyway.ui.components.NewPlaceCard
import com.efbsm5.easyway.ui.components.NewPointCard
import com.efbsm5.easyway.ui.components.ShowSearchScreen
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.Screen

@Composable
fun MapPage(viewModel: MapPageViewModel, mapView: MapView, mapController: MapController) {
    val content by viewModel.content.collectAsState()
    val boxHeight by viewModel.boxHeight.collectAsState()
    val location by viewModel.location.collectAsState()
    val selectedPoi = viewModel.selectedPoi
    MapScreen(mapView = mapView,
        content = content,
        boxHeight = boxHeight,
        onChangeScreen = { viewModel.changeScreen(it) },
        location = location,
        onLocate = {
            viewModel.moveMapToLocation(
                mapView = mapView, mapController = mapController
            )
        },
        onChangeHeight = { viewModel.changeBoxHeight(it) },
        selectedPoi = selectedPoi,
        navigate = {
            viewModel.navigate(
                it,
                context = TODO(),
                mapView = mapView,
            )
        },
        markerList = viewModel.markers.collectAsState().value,
        getPoint = { viewModel.getPoint(it) })
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
    selectedPoi: PoiItemV2?,
    navigate: (LatLng) -> Unit,
    markerList: List<PoiItemV2>,
    getPoint: (String) -> Unit
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
                    MapPageCard()
                }
            },
        )
    }
    BackHandler(enabled = content != Screen.IconCard, onBack = { onChangeScreen(Screen.IconCard) })

}