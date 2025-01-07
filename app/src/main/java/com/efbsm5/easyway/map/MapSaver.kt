package com.efbsm5.easyway.map

import com.amap.api.maps.MapView
import com.efbsm5.easyway.data.EasyPointSimplify

object MapSaver {
    lateinit var mapView: MapView
    lateinit var mapController: MapController

    fun isMapViewInitialized(): Boolean {
        return ::mapView.isInitialized
    }

    fun isMapControllerInitialized(): Boolean {
        return ::mapController.isInitialized
    }
}