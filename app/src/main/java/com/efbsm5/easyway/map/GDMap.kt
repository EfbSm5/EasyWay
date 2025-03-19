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
import androidx.compose.ui.platform.LocalContext
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
import com.efbsm5.easyway.data.models.assistModel.EasyPointSimplify
import com.efbsm5.easyway.ui.theme.isDarkTheme

private const val TAG = "GDMap"

@Composable
fun GDMap(
    onMapClick: AMap.OnMapClickListener,
    onPoiClick: AMap.OnPOIClickListener,
    onMarkerClick: AMap.OnMarkerClickListener,
    locationSource: LocationSource,
    mapState: MapState
) {
    if (LocalInspectionMode.current) {
        return
    }
    val context = LocalContext.current
    val locationSaver = LocationSaver(context)
    val mapView = remember { MapView(context, AMapOptions().compassEnabled(true)) }.apply {
        setMap(
            onMapClick, onMarkerClick, onPoiClick, locationSource, locationSaver.location
        )
    }
    var isLoading by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView }, onRelease = {
            it.onDestroy()
            it.removeAllViews()
        })
        if (isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary, // 进度条的颜色
                strokeWidth = 4.dp // 进度条的宽度
            )
        }
    }
    MapLifecycle(
        mapView = mapView, {}, {}
    )
    LaunchedEffect(mapState) {
        mapView.map.animateCamera(CameraUpdateFactory.newLatLng(locationSaver.location))
        when (mapState) {
            MapState.Gone -> {
                mapView.visibility = View.GONE
            }

            is MapState.Point -> {
                if (mapState.points.isEmpty()) isLoading = true
                else {
                    isLoading = false
                    Log.e(TAG, "GDMap: $mapState")
                    mapView.map.apply {
                        mapType = if (isDarkTheme(context)) MAP_TYPE_NIGHT else MAP_TYPE_NORMAL
                    }
                    drawPoints(mapView, mapState.points)
                }
            }

            is MapState.Route -> {
                isLoading = true
                mapView.map.apply {
                    mapType = if (isDarkTheme(context)) MAP_TYPE_NAVI_NIGHT else MAP_TYPE_NAVI
                }
                startRouteSearch(
                    mapState.endPoint, mapView, context, locationSaver = locationSaver, callBack = {
                        isLoading = false
                    })
            }
        }
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
        setLocationSource(locationSource)
        animateCamera(CameraUpdateFactory.newLatLng(location))
    }
}

sealed interface MapState {
    data class Route(val endPoint: LatLng) : MapState
    data class Point(val points: List<EasyPointSimplify>) : MapState
    data object Gone : MapState
}