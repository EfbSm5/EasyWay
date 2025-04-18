package com.efbsm5.easyway.map

import android.util.Log
import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.MAP_TYPE_NAVI
import com.amap.api.maps.AMap.MAP_TYPE_NAVI_NIGHT
import com.amap.api.maps.AMap.MAP_TYPE_NIGHT
import com.amap.api.maps.AMap.MAP_TYPE_NORMAL
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.efbsm5.easyway.Myapplication
import com.efbsm5.easyway.data.models.assistModel.EasyPointSimplify
import com.efbsm5.easyway.ui.theme.isDarkTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val TAG = "GDMap"

@Composable
fun GDMap(
    onMapClick: AMap.OnMapClickListener,
    onPoiClick: AMap.OnPOIClickListener,
    onMarkerClick: AMap.OnMarkerClickListener,
    locationSource: LocationSource,
    mapState: MapState,
    modifier: Modifier
) {
    if (LocalInspectionMode.current) {
        return
    }
    val mapView =
        remember { MapView(Myapplication.getContext(), AMapOptions().compassEnabled(true)) }
    var isLoading by remember { mutableStateOf(false) }
    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView }, onRelease = {
            it.onDestroy()
            it.removeAllViews()
        })
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary, strokeWidth = 4.dp
            )
        }
    }
    MapLifecycle(mapView = mapView, {
        mapView.apply {
            setMap(
                onMapClick, onMarkerClick, onPoiClick, locationSource, LocationSaver.location
            )
        }
    }, {})
    LaunchedEffect(mapState) {
        mapView.map.animateCamera(CameraUpdateFactory.newLatLng(LocationSaver.location))
        when (mapState) {
            MapState.Gone -> {
                mapView.visibility = View.GONE
            }

            is MapState.Point -> {
                if (mapState.points.isEmpty()) {
                    isLoading = true
                    executeAfterDelay(2000) { isLoading = false }
                } else {
                    isLoading = false
                    Log.e(TAG, "GDMap: $mapState")
                    mapView.map.apply {
                        mapType = if (isDarkTheme()) MAP_TYPE_NIGHT else MAP_TYPE_NORMAL
                    }
                    drawPoints(mapView, mapState.points)
                }
            }

            is MapState.Route -> {
                isLoading = true
                mapView.map.apply {
                    mapType = if (isDarkTheme()) MAP_TYPE_NAVI_NIGHT else MAP_TYPE_NAVI
                }
                startRouteSearch(
                    mapState.endPoint, mapView, locationSaver = LocationSaver,
                    callBack = {
                        isLoading = false
                    },
                )
            }
        }
    }
}

fun executeAfterDelay(delayMillis: Long, action: () -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        delay(delayMillis)
        action()
    }
}

fun drawPoints(mapView: MapView, points: List<EasyPointSimplify>) {
    mapView.map.clear()
    points.forEach { point ->
        mapView.map.addMarker(
            MarkerOptions().title(
                point.name
            ).position(LatLng(point.lat, point.lng)).icon(BitmapDescriptorFactory.defaultMarker())
        )
    }
}

private fun MapView.setMap(
    onMapClick: AMap.OnMapClickListener,
    onMarkerClick: AMap.OnMarkerClickListener,
    onPoiClick: AMap.OnPOIClickListener,
    locationSource: LocationSource,
    location: LatLng
) {
    this.map.apply {
        mapType = if (isDarkTheme()) MAP_TYPE_NIGHT else MAP_TYPE_NORMAL
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
        setLocationSource(locationSource)
        animateCamera(CameraUpdateFactory.newLatLng(location))
    }
}

sealed interface MapState {
    data class Route(val endPoint: LatLng) : MapState
    data class Point(val points: List<EasyPointSimplify>) : MapState
    data object Gone : MapState
}