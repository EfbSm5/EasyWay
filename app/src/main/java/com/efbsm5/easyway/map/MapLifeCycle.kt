package com.efbsm5.easyway.map

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.MAP_TYPE_NIGHT
import com.amap.api.maps.AMap.MAP_TYPE_NORMAL
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle
import com.efbsm5.easyway.ui.theme.isDarkTheme

private const val TAG = "MapLifeCycle"

@Composable
fun MapLifecycle(
    mapView: MapView,
    onMapClick: AMap.OnMapClickListener,
    onPoiClick: AMap.OnPOIClickListener,
    onMarkerClick: AMap.OnMarkerClickListener,
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val locationSaver = LocationSaver(context)
    val locationController = LocationController(locationSaver)
    locationController.initLocation(context)
    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver(onResume = {
            mapView.map.apply {
                mapType = if (isDarkTheme(context)) MAP_TYPE_NIGHT else MAP_TYPE_NORMAL
                isMyLocationEnabled = true
                myLocationStyle = MyLocationStyle().interval(2000)
                    .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)
                showMapText(true)
                uiSettings.apply {
                    isMyLocationButtonEnabled = true
                    zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_CENTER
                }
                setOnMapClickListener(onMapClick)
                setOnPOIClickListener(onPoiClick)
                setOnMarkerClickListener(onMarkerClick)
                setLocationSource(locationController.locationSource)
                animateCamera(CameraUpdateFactory.newLatLng(locationSaver.location))
            }
        }, onPause = {
            mapView.map.apply {
                removeOnMapClickListener(onMapClick)
                removeOnPOIClickListener(onPoiClick)
                removeOnMarkerClickListener(onMarkerClick)
            }
        })
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
    onResume: () -> Unit, onPause: () -> Unit,
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
        }

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