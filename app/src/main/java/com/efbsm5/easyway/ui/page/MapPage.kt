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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amap.api.maps.MapView
import com.efbsm5.easyway.ui.components.AddAndLocateButton
import com.efbsm5.easyway.ui.components.CommentAndHistoryCard
import com.efbsm5.easyway.ui.components.DraggableBox
import com.efbsm5.easyway.ui.components.FunctionCard
import com.efbsm5.easyway.ui.components.NewPlaceCard
import com.efbsm5.easyway.ui.components.NewPointCard
import com.efbsm5.easyway.ui.components.ShowSearchScreen
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.Screen
import com.efbsm5.easyway.viewmodel.ViewModelFactory


@Composable
fun MapPage() {
    val context = LocalContext.current
    val mapPageViewModel = viewModel<MapPageViewModel>(factory = ViewModelFactory(context))
    mapPageViewModel.MapLifecycle(context)
    val content = mapPageViewModel.content.collectAsState().value
    MapScreen(
        mapPageViewModel = mapPageViewModel,
        mapView = mapPageViewModel.mapView.collectAsState().value,
    )
    BackHandler(enabled = content != Screen.IconCard,
        onBack = { mapPageViewModel.changeScreen(Screen.IconCard) })
}

@Composable
private fun MapScreen(
    mapPageViewModel: MapPageViewModel, mapView: MapView
) {
    val content = mapPageViewModel.content.collectAsState().value
    AndroidView(modifier = Modifier
        .fillMaxSize()
        .padding(bottom = mapPageViewModel.boxHeight.collectAsState().value),
        factory = { mapView })
    AddAndLocateButton(onAdd = {
        mapPageViewModel.changeScreen(
            Screen.NewPoint(
                location = mapPageViewModel.location.value
            )
        )
    },
        onLocate = { mapPageViewModel.moveMapToLocation() },
        bottomHeight = mapPageViewModel.boxHeight.collectAsState().value
    )

    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        DraggableBox(
            boxHeight = mapPageViewModel.boxHeight.collectAsState().value,
            onChangeHeight = { mapPageViewModel.changeBoxHeight(it) },
            content = {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    when (content) {
                        is Screen.Comment -> {
                            CommentAndHistoryCard(
                                marker = content.marker, mapPageViewModel.selectedPoi
                            )
                        }

                        Screen.IconCard -> FunctionCard(onclick = {
                            mapPageViewModel.changeScreen(Screen.Places(it))
                        }, onChangePage = { mapPageViewModel.changeScreen(Screen.Search) })

                        is Screen.NewPoint -> NewPointCard(content.location,
                            back = { mapPageViewModel.changeScreen(Screen.IconCard) })

                        is Screen.Places -> {
                            NewPlaceCard(mapPageViewModel.location.collectAsState().value,
                                content.name,
                                onNavigate = {
                                    mapPageViewModel.navigate(it)
                                })
                        }

                        Screen.Search -> ShowSearchScreen(markerList = mapPageViewModel.markers.collectAsState().value,
                            searchForPoi = {
                                mapPageViewModel.getPoint(
                                    title = it
                                )
                            },
                            onSelected = {
                                mapPageViewModel.changeScreen(
                                    Screen.Comment(
                                        marker = null
                                    )
                                )
                            })
                    }
                }
            },
        )
    }

}




