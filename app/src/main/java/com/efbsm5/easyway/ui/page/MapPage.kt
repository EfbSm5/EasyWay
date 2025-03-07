package com.efbsm5.easyway.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(viewModel: MapPageViewModel) {
    val context = LocalContext.current
    val mapView = MapView(context, AMapOptions().compassEnabled(true))
    val mapController = MapController(
        onPoiClick = { viewModel.changeScreen(Screen.Comment(null, it, null)) },
        onMapClick = { },
        onMarkerClick = {
            viewModel.changeScreen(Screen.Comment(it, null, null))
        })
    mapController.mapLocationInit(context)
    mapController.MapLifecycle(context, mapView)
    viewModel.fetchPoints(mapView)
    val content by viewModel.content.collectAsState()
    val location by viewModel.location.collectAsState()
    val sheetState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()
//    DisposableEffect(mapView) {
////        mapView.onCreate(null)
////        mapController.mapLocationInit(context)
////        mapController.MapLifecycle(context, mapView)
////        viewModel.fetchPoints(mapView)
//
//        onDispose {
//            mapView.onDestroy()
//
//        }
//    }
    MapScreen(
        mapView = mapView, content = content, onChangeScreen = {
            viewModel.changeScreen(it)
            scope.launch {
                sheetState.bottomSheetState.expand()
            }
        }, location = location, onLocate = {
            mapController.moveToLocation(mapView)
        }, navigate = { latLng, isNavigate ->
            if (isNavigate) {
                mapController.navigate(latLng, context, mapView)
            } else {
                MapUtil.onNavigate(context, latLng)
            }
        }, sheetState = sheetState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapScreen(
    mapView: MapView,
    content: Screen,
    onChangeScreen: (Screen) -> Unit,
    location: LatLng,
    onLocate: () -> Unit,
    sheetState: BottomSheetScaffoldState,
    navigate: (LatLng, Boolean) -> Unit,
) {
    BottomSheetScaffold(sheetContent = {
        AddAndLocateButton(onAdd = {
            onChangeScreen(
                Screen.NewPoint(
                    location = location
                )
            )
        }, onLocate = { onLocate() })
        MapPageCard(
            content = content,
            onChangeScreen = { onChangeScreen(it) },
            location = location,
            onNavigate = navigate
        )
    }, scaffoldState = sheetState, sheetPeekHeight = 128.dp) {
        AndroidView(
            modifier = Modifier.fillMaxSize(), factory = { mapView })
    }

//    Box(
//        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
//    ) {
//        DraggableBox(
//            boxHeight = boxHeight,
//            onChangeHeight = { onChangeHeight(it) },
//            content = {
//                Surface(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(color = MaterialTheme.colorScheme.surfaceContainer)
//                ) {
//
//                }
//            },
//        )
//    }
    BackHandler(enabled = content != Screen.IconCard, onBack = { onChangeScreen(Screen.IconCard) })

}