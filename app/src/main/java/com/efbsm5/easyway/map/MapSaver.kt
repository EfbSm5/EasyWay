package com.efbsm5.easyway.map

import android.content.Context
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.Poi

object MapSaver {
    lateinit var mapView: MapView
    lateinit var mapController: MapController
    private fun isMapViewInitialized(): Boolean {
        return ::mapView.isInitialized
    }

    fun isMapControllerInitialized(): Boolean {
        return ::mapController.isInitialized
    }

    fun initializeMapView(context: Context) {
        if (!isMapViewInitialized()) {
            mapView = MapView(context, AMapOptions().compassEnabled(true))
        }
    }


    fun initializeMapController(
        onPoiClick: (Poi?) -> Unit,
        onMapClick: (LatLng?) -> Unit,
        onMarkerClick: (Marker?) -> Unit
    ) {
        if (!isMapControllerInitialized()) {
            mapController = MapController(onPoiClick, onMapClick, onMarkerClick)
        }
    }

    private fun getIcon(): BitmapDescriptor {
        return BitmapDescriptorFactory.defaultMarker(5f)
    }

//    @Composable
//    private fun InitPoints() {
//        val pointsViewModel = viewModel<PointsViewModel>()
//        val points by pointsViewModel.points.collectAsState()
//        LaunchedEffect(points) {
//            points?.forEach {
//                mapView.map.addMarker(
//                    MarkerOptions().position(LatLng(it.lat, it.lng)).title(it.name)
//                )
//            }
//            //        mapView.map.addMarker(
////            MarkerOptions().position(LatLng(30.513447, 114.426866)).title("666")
////                .icon(BitmapDescriptorFactory.defaultMarker())
////        )
//        }
//    }
}