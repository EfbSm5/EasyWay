package com.efbsm5.easyway.map

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
    onPoiClick: (Poi?) -> Unit, onMapClick: (LatLng?) -> Unit, onMarkerClick: (Marker?) -> Unit
) : LocationSource, AMap.OnMapClickListener, AMap.OnPOIClickListener, AMap.OnMarkerClickListener,
    AMapLocationListener {
    private lateinit var mLocationClient: AMapLocationClient
    private lateinit var sharedPreferences: SharedPreferences
    private var mLocationOption =
        AMapLocationClientOption().setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
            .setOnceLocation(true).setOnceLocationLatest(true).setNeedAddress(true)
            .setHttpTimeOut(6000)
    private var mListener: OnLocationChangedListener? = null
    val monMapClick: (LatLng?) -> Unit = onMapClick
    val monPoiClick: (Poi?) -> Unit = onPoiClick
    val monMarkerClick: (Marker?) -> Unit = onMarkerClick
    private var mLocation: LatLng? = null
    private var isDarkTheme: Boolean? = null

    private fun initializeVariables(context: Context) {
        isDarkTheme = isDarkTheme(context)
        sharedPreferences = context.getSharedPreferences("MapPreferences", Context.MODE_PRIVATE)
        mLocationClient = AMapLocationClient(context)
        mLocation = getLastKnownLocation()
    }

    fun getLastKnownLocation(): LatLng {
        val lat = sharedPreferences.getFloat("last_lat", Float.NaN)
        val lng = sharedPreferences.getFloat("last_lng", Float.NaN)
        return LatLng(lat.toDouble(), lng.toDouble())
    }

    private fun saveLastKnownLocation(location: LatLng, cityCode: String) {
        sharedPreferences.edit().putFloat("last_lat", location.latitude.toFloat())
            .putFloat("last_lng", location.longitude.toFloat()).putString("citycode", cityCode)
            .apply()
    }


    fun mapLocationInit(context: Context) {
        initializeVariables(context)
        mLocationClient.setLocationOption(mLocationOption)
        mLocationClient.setLocationListener(this@MapController)
    }

    fun onLocate(mapView: MapView) {
        mLocation?.let {
            moveMap(it, mapView)
        }
    }

    private fun moveMap(latLng: LatLng, mapView: MapView) {
        mapView.map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    fun initMap(mapView: MapView) {
        val map = mapView.map
        map.mapType = if (isDarkTheme!!) MAP_TYPE_NIGHT else MAP_TYPE_NORMAL
        map.setLocationSource(this@MapController)
        map.isMyLocationEnabled = true
        map.myLocationStyle =
            MyLocationStyle().interval(2000).myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW)
        map.setOnMapClickListener(this@MapController)
        map.setOnPOIClickListener(this@MapController)
        map.setOnMarkerClickListener(this@MapController)
        map.showMapText(true)
        getLastKnownLocation().let { map.animateCamera(CameraUpdateFactory.newLatLng(it)) }
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
            saveLastKnownLocation(mLocation!!, aMapLocation.cityCode)
        }
    }
}