package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.Context
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MapPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _content = MutableStateFlow<Screen>(Screen.IconCard)
    private var _boxHeight = MutableStateFlow(100.dp)
    private val _location = MutableStateFlow(LatLng(30.513197, 114.413301))
    val content: StateFlow<Screen> = _content
    val boxHeight: StateFlow<Dp> = _boxHeight
    val location: StateFlow<LatLng> = _location
    fun fetchPoints(mapView: MapView) {
        viewModelScope.launch(Dispatchers.IO) {
            val points = repository.getAllPoints()
            points.forEach { point ->
                mapView.map.addMarker(
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

}

sealed interface Screen {
    data object IconCard : Screen
    data class NewPoint(val location: LatLng?) : Screen
    data class Places(val name: String) : Screen
    data class Comment(val marker: Marker?, val poiItemV2: PoiItemV2?) : Screen
    data object Search : Screen
}