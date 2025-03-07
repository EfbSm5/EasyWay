package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.Poi
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import com.efbsm5.easyway.map.LocationController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn


class MapPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _content = MutableStateFlow<Screen>(Screen.IconCard)
    private val _location = MutableStateFlow(LatLng(30.513197, 114.413301))
    private val _points =
        repository.getAllPoints().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    private val locationController = LocationController(context)
    val content: StateFlow<Screen> = _content
    val location: StateFlow<LatLng> = _location

    fun drawPointsAndInitLocation(mapView: MapView) {
        locationController.mapLocationInit(
            mapView = mapView
        )
        mapView.map.clear()
        _points.value.forEach { point ->
            mapView.map.addMarker(
                MarkerOptions().title(
                    point.name
                ).position(LatLng(point.lat, point.lng))
                    .icon(BitmapDescriptorFactory.defaultMarker())
            )
        }
    }

    fun changeScreen(screen: Screen) {
        _content.value = screen
    }

    fun moveToLocation(mapView: MapView) {
        locationController.moveToLocation(mapView)
    }

    fun navigate(context: Context, destination: LatLng, mapView: MapView) {
        locationController.navigate(
            context = context, latLng = destination, mapView = mapView
        )
    }
}

sealed interface Screen {
    data object IconCard : Screen
    data class NewPoint(val location: LatLng?) : Screen
    data class Places(val name: String) : Screen
    data class Comment(val marker: Marker?, val poi: Poi?, val poiItemV2: PoiItemV2?) : Screen
    data object Search : Screen
}