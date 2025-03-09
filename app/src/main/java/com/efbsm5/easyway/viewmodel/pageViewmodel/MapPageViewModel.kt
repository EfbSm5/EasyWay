package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.Poi
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.Repository.DataRepository
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.models.assistModel.EasyPointSimplify
import com.efbsm5.easyway.map.LocationController
import com.efbsm5.easyway.map.MapRouteSearchUtil
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.ui.components.mapcards.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class MapPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private val locationController = LocationController(context)
    private val _state = MutableStateFlow<Screen>(Screen.IconCard)
    val location = locationController.location
    val state: StateFlow<Screen> = _state

    fun drawPointsAndInitLocation(mapView: MapView) {
        locationController.mapLocationInit(mapView)
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPoints().collect {
                mapView.map.clear()
                it.forEach { point ->
                    mapView.map.addMarker(
                        MarkerOptions().title(
                            point.name
                        ).position(LatLng(point.lat, point.lng))
                            .icon(BitmapDescriptorFactory.defaultMarker())
                    )
                }
            }
        }
        mapView.map.animateCamera(CameraUpdateFactory.newLatLng(locationController.getLastKnownLocation()))
    }

    fun changeScreen(screen: Screen) {
        _state.value = screen
    }

    fun navigate(context: Context, destination: LatLng, mapView: MapView) {
        MapRouteSearchUtil(
            mapView = mapView,
            context = context,
            returnMsg = { MapUtil.showMsg(it, context) }).startRouteSearch(
            mStartPoint = location.value, mEndPoint = destination
        )
    }
}
