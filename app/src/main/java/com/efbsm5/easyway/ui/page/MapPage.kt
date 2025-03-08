package com.efbsm5.easyway.ui.page

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.Poi
import com.efbsm5.easyway.map.MapUtil.initMapView
import com.efbsm5.easyway.map.MapUtil.onNavigate
import com.efbsm5.easyway.ui.components.AddAndLocateButton
import com.efbsm5.easyway.ui.components.mapcards.MapPageCard
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.Screen
import kotlinx.coroutines.launch

private const val TAG = "MapPage"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(viewModel: MapPageViewModel) {
    val context = LocalContext.current
    val mapView = remember { initMapView(context) }
    viewModel.drawPointsAndInitLocation(mapView)
    val content by viewModel.content.collectAsState()
    val location by viewModel.location.collectAsState()
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden, skipHiddenState = false
        )
    )
    val scope = rememberCoroutineScope()
    MapScreen(
        mapView = mapView,
        content = content,
        onChangeScreen = {
            viewModel.changeScreen(it)
            scope.launch {
                sheetState.bottomSheetState.expand()
            }
        },
        location = location,
        onLocate = {
            viewModel.moveToLocation(mapView)
        },
        navigate = { latLng, isNavigate ->
            if (isNavigate) {
                viewModel.navigate(context, latLng, mapView)
            } else {
                onNavigate(context, latLng)
            }
        },
        sheetState = sheetState,
        onPoiClick = { viewModel.changeScreen(Screen.Comment(null, it, null)) },
        onMapClick = { },
        onMarkerClick = {
            viewModel.changeScreen(Screen.Comment(it, null, null))
        })
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
    onMapClick: (LatLng) -> Unit,
    onPoiClick: (Poi) -> Unit,
    onMarkerClick: (Marker) -> Unit
) {
    BottomSheetScaffold(sheetContent = {
        Box {
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
        }
    }, scaffoldState = sheetState, sheetPeekHeight = 128.dp) {
        AndroidView(
            modifier = Modifier.fillMaxSize(), factory = { mapView })
        MapLifecycle(
            mapView,
            onMapClick = { onMapClick(it) },
            onPoiClick = { onPoiClick(it) },
            onMarkerClick = { onMarkerClick(it) })
    }
    BackHandler(enabled = content != Screen.IconCard, onBack = { onChangeScreen(Screen.IconCard) })
}

@Composable
fun MapLifecycle(
    mapView: MapView,
    onMapClick: (LatLng) -> Unit,
    onPoiClick: (Poi) -> Unit,
    onMarkerClick: (Marker) -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver(onResume = {
            mapView.map.apply {
                setOnMapClickListener {
                    onMapClick(it)
                }
                setOnPOIClickListener {
                    onPoiClick(it)
                }
                setOnMarkerClickListener {
                    onMarkerClick(it)
                    true
                }
            }
        }, onPause = {
            mapView.map.apply {
                setOnMapClickListener(null)
                setOnPOIClickListener(null)
                setOnMarkerClickListener(null)
            }
        })
        val callbacks = mapView.componentCallbacks()
        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)
        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)
        }
    }
}

private fun MapView.lifecycleObserver(
    onResume: () -> Unit, onPause: () -> Unit
): LifecycleEventObserver = LifecycleEventObserver { _, event ->
    when (event) {
        Lifecycle.Event.ON_CREATE -> {
            this.onCreate(Bundle())
            Log.e(TAG, "lifecycleObserver:     oncreate view")
        }

        Lifecycle.Event.ON_RESUME -> {
            onResume()
            this.onResume()
            Log.e(TAG, "lifecycleObserver:           onresume view")
        }

        Lifecycle.Event.ON_PAUSE -> {
            onPause()
            this.onPause()
            Log.e(TAG, "lifecycleObserver:                on pause view")
        }  // 暂停地图的绘制
        Lifecycle.Event.ON_DESTROY -> {
            this.onDestroy()
            Log.e(TAG, "lifecycleObserver:            on destory view")
        } // 销毁地图
        else -> {}
    }
}

private fun MapView.componentCallbacks(): ComponentCallbacks = object : ComponentCallbacks {
    override fun onConfigurationChanged(config: Configuration) {}

    @Deprecated("Deprecated in Java", ReplaceWith("this@componentCallbacks.onLowMemory()"))
    override fun onLowMemory() {
        this@componentCallbacks.onLowMemory()
    }
}
