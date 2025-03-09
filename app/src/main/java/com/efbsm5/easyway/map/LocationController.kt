package com.efbsm5.easyway.map

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.LocationSource.OnLocationChangedListener
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import androidx.core.content.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "LocationController"
class LocationController(
    context: Context
) : LocationSource {
    private var mLocationClient = AMapLocationClient(context).apply {
        setLocationOption(
            AMapLocationClientOption().setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                .setOnceLocation(true).setOnceLocationLatest(true).setNeedAddress(true)
                .setHttpTimeOut(6000)
        )
    }
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MapPreferences", Context.MODE_PRIVATE)
    private var mListener: OnLocationChangedListener? = null
    private val _location = MutableStateFlow(LatLng(30.507950, 114.413514))
    val location: StateFlow<LatLng> = _location

    fun getLastKnownLocation(): LatLng {
        Log.e(TAG, "getLastKnownLocation:       get", )
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
        Log.e(TAG, "saveLastKnownLocation:   save", )
    }

    fun mapLocationInit(mapView: MapView) {
        mapView.map.setLocationSource(this)
        mLocationClient.setLocationListener { aMapLocation ->
            if (aMapLocation!!.errorCode == 0) {
                mListener!!.onLocationChanged(aMapLocation)
                _location.value = LatLng(aMapLocation.latitude, aMapLocation.longitude)
                saveLastKnownLocation(_location.value, aMapLocation.cityCode)
                Log.e(TAG, "saveLastKnownLocation:   locate", )

            }
        }
    }

    fun moveToLocation(mapView: MapView) {
        try {
            mapView.moveMap(_location.value)
            mapView.moveMap(getLastKnownLocation())
            mapView.moveMap(MapUtil.locationToLatlng(mapView.map.myLocation))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun MapView.moveMap(latLng: LatLng) {
        this.map.animateCamera(CameraUpdateFactory.newLatLng(latLng))
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
}