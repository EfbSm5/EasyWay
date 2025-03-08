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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.AMap.MAP_TYPE_NIGHT
import com.amap.api.maps.AMap.MAP_TYPE_NORMAL
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.maps.model.Poi
import com.efbsm5.easyway.map.MapUtil.initMapView
import com.efbsm5.easyway.map.MapUtil.onNavigate
import com.efbsm5.easyway.ui.components.AddAndLocateButton
import com.efbsm5.easyway.ui.components.mapcards.MapPageCard
import com.efbsm5.easyway.ui.theme.isDarkTheme
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.Screen
import kotlinx.coroutines.launch

private const val TAG = "MapPage"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(viewModel: MapPageViewModel) {
    val context = LocalContext.current
    val content by viewModel.content.collectAsState()
    val location by viewModel.location.collectAsState()
    val bundle = viewModel.bundle
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )
    val mapView = initMapView(context)
    viewModel.drawPointsAndInitLocation(mapView)
    val scope = rememberCoroutineScope()
    MapScreen(
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
        mapView = mapView,
        onPoiClick = { viewModel.changeScreen(Screen.Comment(null, it, null)) },
        onMapClick = { },
        onMarkerClick = {
            viewModel.changeScreen(Screen.Comment(it, null, null))
        },
        bundle = bundle,
        onSaveBundle = { viewModel.saveBundle(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapScreen(
    content: Screen,
    onChangeScreen: (Screen) -> Unit,
    location: LatLng,
    onLocate: () -> Unit,
    sheetState: BottomSheetScaffoldState,
    navigate: (LatLng, Boolean) -> Unit,
    mapView: MapView,
    onMapClick: (LatLng) -> Unit,
    onPoiClick: (Poi) -> Unit,
    onMarkerClick: (Marker) -> Unit,
    bundle: Bundle,
    onSaveBundle: (Bundle) -> Unit
) {
    BottomSheetScaffold(sheetContent = {
        Box {
            AddAndLocateButton(onAdd = {
                onChangeScreen(
                    Screen.NewPoint(
                        location = location
                    )
                )
            }, onLocate = onLocate)
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
            onMapClick = onMapClick,
            onPoiClick = onPoiClick,
            onMarkerClick = onMarkerClick,
            mapView = mapView,
            bundle = bundle,
            onSaveBundle = onSaveBundle
        )
    }
    BackHandler(enabled = content != Screen.IconCard, onBack = { onChangeScreen(Screen.IconCard) })
}

@Composable
fun MapLifecycle(
    bundle: Bundle,
    mapView: MapView,
    onMapClick: (LatLng) -> Unit,
    onPoiClick: (Poi) -> Unit,
    onMarkerClick: (Marker) -> Unit,
    onSaveBundle: (Bundle) -> Unit
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver(
            onResume = {
                mapView.map.apply {
                    mapType = if (isDarkTheme(context)) MAP_TYPE_NIGHT else MAP_TYPE_NORMAL
                    isMyLocationEnabled = true
                    myLocationStyle = MyLocationStyle().interval(2000)
                        .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)
                    showMapText(true)
                    uiSettings.apply {
                        isMyLocationButtonEnabled = true
                        zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_CENTER
                    }
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
                val tempBundle = Bundle()
                mapView.onSaveInstanceState(tempBundle)
                onSaveBundle(tempBundle)
                mapView.map.apply {
                    setOnMapClickListener(null)
                    setOnPOIClickListener(null)
                    setOnMarkerClickListener(null)
                }
                mapView.removeAllViews()
            }, bundle = bundle
        )
        val callbacks = mapView.componentCallbacks()
        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)
        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
            Log.e(TAG, "MapLifecycle:      remove")
            context.unregisterComponentCallbacks(callbacks)
        }
    }
}

private fun MapView.lifecycleObserver(
    onResume: () -> Unit, onPause: () -> Unit, bundle: Bundle
): LifecycleEventObserver = LifecycleEventObserver { _, event ->
    when (event) {
        Lifecycle.Event.ON_CREATE -> {
            this.onCreate(bundle)
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