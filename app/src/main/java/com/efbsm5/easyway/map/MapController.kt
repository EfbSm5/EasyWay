package com.efbsm5.easyway.map

import android.content.ComponentCallbacks
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.AMap.MAP_TYPE_NIGHT
import com.amap.api.maps.AMap.MAP_TYPE_NORMAL
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.LocationSource.OnLocationChangedListener
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MyLocationStyle
import com.amap.api.maps.model.Poi
import com.efbsm5.easyway.ui.theme.isDarkTheme
import androidx.core.content.edit

private const val TAG = "MapController"

class MapController(
    val onPoiClick: (Poi) -> Unit,
    val onMapClick: (LatLng) -> Unit,
    val onMarkerClick: (Marker) -> Unit
) : LocationSource {
    private lateinit var mLocationClient: AMapLocationClient
    private lateinit var sharedPreferences: SharedPreferences
    private var mLocationOption =
        AMapLocationClientOption().setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
            .setOnceLocation(true).setOnceLocationLatest(true).setNeedAddress(true)
            .setHttpTimeOut(6000)
    private var mListener: OnLocationChangedListener? = null
    private var mLocation = LatLng(30.507950, 114.413514)
    private var isDarkTheme = false

    private fun initializeVariables(context: Context) {
        Log.e(TAG, "initializeVariables:   ")
        isDarkTheme = isDarkTheme(context)
        sharedPreferences = context.getSharedPreferences("MapPreferences", Context.MODE_PRIVATE)
        mLocationClient = AMapLocationClient(context)
        mLocation = getLastKnownLocation()
    }

    private fun getLastKnownLocation(): LatLng {
        val lat = sharedPreferences.getFloat("last_lat", Float.NaN)
        val lng = sharedPreferences.getFloat("last_lng", Float.NaN)
        return LatLng(lat.toDouble(), lng.toDouble())
    }

    private fun saveLastKnownLocation(location: LatLng, cityCode: String) {
        sharedPreferences.edit {
            putFloat("last_lat", location.latitude.toFloat()).putFloat(
                "last_lng", location.longitude.toFloat()
            ).putString("citycode", cityCode)
        }
    }


    fun mapLocationInit(context: Context) {
        initializeVariables(context)
        mLocationClient.setLocationOption(mLocationOption)
        mLocationClient.setLocationListener { aMapLocation ->
            if (aMapLocation!!.errorCode == 0) {
                mListener!!.onLocationChanged(aMapLocation)
                mLocation = LatLng(aMapLocation.latitude, aMapLocation.longitude)
                saveLastKnownLocation(mLocation, aMapLocation.cityCode)
                Log.e(TAG, "mapLocationInit:     location")
            }
        }
    }

    fun moveToLocation(mapView: MapView) {
        try {
            moveMap(getLastKnownLocation(), mapView)
            moveMap(mLocation, mapView)
            moveMap(MapUtil.locationToLatlng(mapView.map.myLocation), mapView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun navigate(destination: LatLng, context: Context, mapView: MapView) {
        MapRouteSearchUtil(
            mapView = mapView,
            context = context,
            returnMsg = { MapUtil.showMsg(it, context) }).startRouteSearch(
            mStartPoint = mLocation, mEndPoint = destination
        )
    }

    private fun moveMap(latLng: LatLng, mapView: MapView) {
        mapView.map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    private fun initMap(mapView: MapView) {
        mapView.map.apply {
            mapType = if (isDarkTheme) MAP_TYPE_NIGHT else MAP_TYPE_NORMAL
            setLocationSource(this@MapController)
            isMyLocationEnabled = true
            myLocationStyle = MyLocationStyle().interval(2000)
                .myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
            setOnMapClickListener {
                onMapClick(it)
            }
            setOnPOIClickListener {
                onPoiClick(it)
            }
            setOnMarkerClickListener {
                onMarkerClick(it)
                true
            }
            showMapText(true)
            animateCamera(CameraUpdateFactory.newLatLng(getLastKnownLocation()))
        }
    }

    override fun activate(p0: OnLocationChangedListener?) {
        if (mListener == null) {
            mListener = p0
        }
        mLocationClient.startLocation()
    }

    override fun deactivate() {
        mListener = null
        mLocationClient.stopLocation()
        mLocationClient.onDestroy()
    }

    @Composable
    fun MapLifecycle(context: Context, mapView: MapView) {
        val lifecycle = LocalLifecycleOwner.current.lifecycle
        DisposableEffect(context, lifecycle, this) {
            val mapLifecycleObserver = lifecycleObserver(mapView)
            val callbacks = mapView.componentCallbacks()
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
                    initMap(mapView)
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