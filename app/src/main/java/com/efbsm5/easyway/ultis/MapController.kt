package com.efbsm5.easyway.ultis

import android.annotation.SuppressLint
import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.amap.api.maps.AMap
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


class MapController(
    onPoiClick: (Poi?) -> Unit,
    onMapClick: (LatLng?) -> Unit,
    onMarkerClick: (Marker?) -> Unit,
    context: Context
) : LocationSource, AMap.OnMapClickListener, AMap.OnPOIClickListener, AMap.OnMarkerClickListener,
    AMapLocationListener {
    private var mLocationOption =
        AMapLocationClientOption().setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
            .setOnceLocation(true).setOnceLocationLatest(true).setNeedAddress(true)
            .setHttpTimeOut(6000)
    private var mLocationClient: AMapLocationClient = AMapLocationClient(context)
    private var mListener: OnLocationChangedListener? = null
    val monMapClick: (LatLng?) -> Unit = onMapClick
    val monPoiClick: (Poi?) -> Unit = onPoiClick
    val monMarkerClick: (Marker?) -> Unit = onMarkerClick
    private var mLocation: LatLng? = null
    private var mContext = context
    //private var points: ArrayList<Marker> = ArrayList()


    @Composable
    fun MapLifecycle(mapView: MapView) {
        mLocationClient.setLocationOption(mLocationOption)
        mLocationClient.setLocationListener(this@MapController)
        val context = LocalContext.current
        val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
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

    fun onLocate(mapView: MapView) {
        mLocation?.let {
            mapView.map.animateCamera(CameraUpdateFactory.newLatLng(it))
        }
    }

    private fun lifecycleObserver(mapView: MapView): LifecycleEventObserver =
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    mapView.onCreate(Bundle())
                    initMap(mapView)
                }

                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                }

                Lifecycle.Event.ON_PAUSE -> mapView.onPause()  // 暂停地图的绘制
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy() // 销毁地图
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

    private fun initMap(mapView: MapView) {
        val map = mapView.map
        map.mapType = if (isDarkTheme(mContext)) MAP_TYPE_NIGHT else MAP_TYPE_NORMAL
        map.setLocationSource(this@MapController)
        map.isMyLocationEnabled = true
        map.myLocationStyle =
            MyLocationStyle().interval(2000).myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        map.setOnMapClickListener(this@MapController)
        map.setOnPOIClickListener(this@MapController)
        map.setOnMarkerClickListener(this@MapController)
        map.showMapText(true)
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

    override fun onMapClick(p0: LatLng?) {
        monMapClick(p0)
    }

    override fun onPOIClick(p0: Poi?) {
        monPoiClick(p0)
    }

    @SuppressLint("ResourceType", "InflateParams")
    override fun onMarkerClick(marker: Marker?): Boolean {

        monMarkerClick(marker)
        return true
    }


    override fun onLocationChanged(aMapLocation: AMapLocation?) {
        if (aMapLocation!!.errorCode == 0) {
            mListener!!.onLocationChanged(aMapLocation)
            val latitude = aMapLocation.latitude
            val longitude = aMapLocation.longitude
            mLocation = LatLng(latitude, longitude)
        }
    }
}



