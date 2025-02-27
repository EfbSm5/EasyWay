package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import com.efbsm5.easyway.data.models.assistModel.Markers
import com.efbsm5.easyway.map.MapController
import com.efbsm5.easyway.map.MapSearchUtil
import com.efbsm5.easyway.map.MapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _mapView = MutableStateFlow(MapView(context, AMapOptions().compassEnabled(true)))
    private var _content = MutableStateFlow<Screen>(Screen.IconCard)
    private var _boxHeight = MutableStateFlow(100.dp)
    val mapController = MapController(onPoiClick = {}, onMapClick = {}, onMarkerClick = {})
    private val _location = MutableStateFlow<LatLng?>(null)
    private val _marker = MutableStateFlow<List<Markers>>(emptyList())
    val mapView: StateFlow<MapView> = _mapView
    val content: StateFlow<Screen> = _content
    val boxHeight: StateFlow<Dp> = _boxHeight
    val location: StateFlow<LatLng?> = _location
    val markers: StateFlow<List<Markers>> = _marker


    init {
        fetchPoints()
        mapController.mapLocationInit(context)
        getLocation()
    }

    private fun fetchPoints() {
        viewModelScope.launch(Dispatchers.IO) {
            val points = repository.getAllPoints()
            points.forEach { point ->
                _mapView.value.map.addMarker(
                    MarkerOptions().title(
                        point.name
                    ).position(LatLng(point.lat, point.lng))
                        .icon(BitmapDescriptorFactory.defaultMarker())
                )
            }
        }
    }

    fun changeScreen(screen: Screen) {
        _content.value = screen
    }

    fun changeBoxHeight(height: Dp) {
        _boxHeight.value = height
    }

    private fun getLocation() {
        _location.value = mapController.getLastKnownLocation()
    }
    //似乎应该加一个定时更新location

    fun moveMapToLocation() {
        mapController.onLocate(_mapView.value)
    }

    fun getPoint(title: String, context: Context) {
        val mapSeach = MapSearchUtil(
            context = context,
            mapView = _mapView.value,
            onPoiSearched = { _marker.value = it },
            returnMsg = { MapUtil.showMsg(it, context) },
            returnPosition = {}
        )
        mapSeach.searchForPoi(title)
    }
}

sealed interface Screen {
    data object IconCard : Screen
    data class NewPoint(val location: LatLng?) : Screen
    data class Places(val name: String) : Screen
    data class Comment(val marker: Marker) : Screen
    data object Search : Screen
}