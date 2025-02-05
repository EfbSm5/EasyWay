package com.efbsm5.easyway.viewmodel

import android.content.Context

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.efbsm5.easyway.data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private val _mapView = MutableStateFlow<MapView?>(null)
    val mapView: StateFlow<MapView?> = _mapView

    init {
        _mapView.value = MapView(context, AMapOptions().compassEnabled(true))
        fetchPoints(context)
    }

    private fun fetchPoints(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val points = repository.getAllPoints()
            points.forEach { point ->
                _mapView.value!!.map.addMarker(
                    MarkerOptions().title(
                        point.name
                    ).position(LatLng(point.lat, point.lng))
                        .icon(BitmapDescriptorFactory.defaultMarker())
                )
            }
        }
    }

}