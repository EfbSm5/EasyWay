package com.efbsm5.easyway.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amap.api.maps.MapView
import com.efbsm5.easyway.ui.components.AddAndLocateButton
import com.efbsm5.easyway.ui.components.CommentAndHistoryCard
import com.efbsm5.easyway.ui.components.DraggableBox
import com.efbsm5.easyway.ui.components.FunctionCard
import com.efbsm5.easyway.ui.components.NewPlaceCard
import com.efbsm5.easyway.ui.components.NewPointCard
import com.efbsm5.easyway.viewmodel.MapPageViewModel
import com.efbsm5.easyway.viewmodel.Screen
import com.efbsm5.easyway.viewmodel.ViewModelFactory


@Composable
fun MapPage() {
    val context = LocalContext.current
    val mapPageViewModel = viewModel<MapPageViewModel>(factory = ViewModelFactory(context))
    val content by mapPageViewModel.content.collectAsState()
    val boxHeight by mapPageViewModel.boxHeight.collectAsState()
    val mapView by mapPageViewModel.mapView.collectAsState()
    val mapController = mapPageViewModel.mapController
    mapController?.InitMapLifeAndLocation(mapView!!, context)
    MapScreen(
        onAdd = { mapPageViewModel.changeScreen(Screen.NewPoint(mapController?.getLastKnownLocation())) },
        onLocate = { mapController?.onLocate(mapView!!) },
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
                        mapPageViewModel.changeScreen(Screen.Places(it))
                    })

                    is Screen.NewPoint -> NewPointCard((content as Screen.NewPoint).location,
                        back = { mapPageViewModel.changeScreen(Screen.IconCard) })

                    is Screen.Places -> {
                        NewPlaceCard(
                            mapController?.getLastKnownLocation()!!,
                            (content as Screen.Places).name,
                        )
                    }

                }
            }
        },
        boxHeight = boxHeight,
        onChangeHeight = { mapPageViewModel.changeBoxHeight(it) },
        mapView = mapView!!,
    )
    BackHandler(
        enabled = content != Screen.IconCard,
        onBack = { mapPageViewModel.changeScreen(Screen.IconCard) })
}

@Composable
private fun MapScreen(
    onAdd: () -> Unit,
    onLocate: () -> Unit,
    mapView: MapView,
    boxHeight: Dp,
    onChangeHeight: (Dp) -> Unit,
    content: @Composable () -> Unit
) {
    AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView })
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




