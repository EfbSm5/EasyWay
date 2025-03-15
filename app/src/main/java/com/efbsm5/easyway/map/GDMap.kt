package com.efbsm5.easyway.map

import android.view.View
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.AMap
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.efbsm5.easyway.data.models.assistModel.EasyPointSimplify

@Composable
fun GDMap(
    onMapClick: AMap.OnMapClickListener,
    onPoiClick: AMap.OnPOIClickListener,
    onMarkerClick: AMap.OnMarkerClickListener,
    mapState: MapState
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context, AMapOptions().compassEnabled(true)) }
    val locationSaver = LocationSaver(context)
    MapLifecycle(
        mapView = mapView,
        onPoiClick = onPoiClick,
        onMapClick = onMapClick,
        onMarkerClick = onMarkerClick,
    )
    AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView }, onRelease = {
        mapView.onDestroy()
        mapView.removeAllViews()
    })

    LaunchedEffect(mapState) {
        mapView.map.animateCamera(CameraUpdateFactory.newLatLng(locationSaver.location))
        when (mapState) {
            MapState.Gone -> {
                mapView.visibility = View.GONE
            }

            is MapState.Point -> {
                drawPoints(mapView, mapState.points)
            }

            is MapState.Route -> {
                startRouteSearch(mapState.endPoint, mapView, context)
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

sealed interface MapState {
    data class Route(val endPoint: LatLng) : MapState
    data class Point(val points: List<EasyPointSimplify>) : MapState
    data object Gone : MapState
}