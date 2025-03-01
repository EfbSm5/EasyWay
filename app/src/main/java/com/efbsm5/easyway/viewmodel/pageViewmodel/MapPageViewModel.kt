package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
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
import com.efbsm5.easyway.map.MapController
import com.efbsm5.easyway.map.MapPoiSearchUtil
import com.efbsm5.easyway.map.MapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

private const val TAG = "MapPageViewModel"

class MapPageViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _mapView = MutableStateFlow(MapView(context, AMapOptions().compassEnabled(true)))
    private var _content = MutableStateFlow<Screen>(Screen.IconCard)
    private var _boxHeight = MutableStateFlow(100.dp)
    private val mapController = MapController(onPoiClick = {
        _content.value = Screen.Comment(null)
        _mapView.value.map.animateCamera(CameraUpdateFactory.newLatLng(it?.position)
    }, onMapClick = {
        _mapView.value.map.animateCamera(CameraUpdateFactory.newLatLng(it!!))
    }, onMarkerClick = {
        _content.value = Screen.Comment(it)
        _mapView.value.map.animateCamera(CameraUpdateFactory.newLatLng(it?.position))
    })
    private val _location = MutableStateFlow<LatLng>(LatLng(30.513197, 114.413301))
    private val poiItem = MutableStateFlow<List<PoiItemV2>>(emptyList())
    val mapView: StateFlow<MapView> = _mapView
    val content: StateFlow<Screen> = _content
    val boxHeight: StateFlow<Dp> = _boxHeight
    val location: StateFlow<LatLng> = _location
    val markers: StateFlow<List<PoiItemV2>> = poiItem
    val selectedPoi: PoiItemV2? = null
    private val mapSearch = MapPoiSearchUtil(context = context,
        mapView = _mapView.value,
        onPoiSearched = { poiItem.value = it },
        returnMsg = { MapUtil.showMsg(it, context) })


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

    fun moveMapToLocation() {
        mapController.onLocate(_mapView.value)
    }

    fun getPoint(title: String) {
        mapSearch.searchForPoi(title)
    }

    @Composable
    fun MapLifecycle(context: Context) {
        val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
        DisposableEffect(context, lifecycle, this) {
            val mapLifecycleObserver = lifecycleObserver(_mapView.value)
            val callbacks = _mapView.value.componentCallbacks()
            lifecycle.addObserver(mapLifecycleObserver)
            context.registerComponentCallbacks(callbacks)
            onDispose {
                lifecycle.removeObserver(mapLifecycleObserver)
                context.unregisterComponentCallbacks(callbacks)
            }
        }
    }

    private fun lifecycleObserver(mapView: MapView): LifecycleEventObserver =
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    mapView.onCreate(Bundle())
                    mapController.initMap(mapView)
                    Log.e(TAG, "lifecycleObserver:     oncreate view")
                }

                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                    Log.e(TAG, "lifecycleObserver:           onresume view")
                }

                Lifecycle.Event.ON_PAUSE -> {
                    mapView.onPause()
                    Log.e(TAG, "lifecycleObserver:                on pause view")
                }  // 暂停地图的绘制
                Lifecycle.Event.ON_DESTROY -> {
                    mapView.onDestroy()
                    Log.e(TAG, "lifecycleObserver:            on destory view")
                } // 销毁地图
                else -> {}
            }
        }

    private fun MapView.componentCallbacks(): ComponentCallbacks = object : ComponentCallbacks {
        override fun onConfigurationChanged(config: Configuration) {}

        @Deprecated("Deprecated in Java", ReplaceWith("this@componentCallbacks.onLowMemory()"))
        override fun onLowMemory() {
            this@componentCallbacks.onLowMemory()
        }
    }
}

sealed interface Screen {
    data object IconCard : Screen
    data class NewPoint(val location: LatLng?) : Screen
    data class Places(val name: String) : Screen
    data class Comment(val marker: Marker?, val poiItemV2: PoiItemV2?) : Screen
    data object Search : Screen
}