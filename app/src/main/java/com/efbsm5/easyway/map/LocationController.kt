package com.efbsm5.easyway.map

import android.content.Context
import android.content.SharedPreferences
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.LocationSource
import com.amap.api.maps.LocationSource.OnLocationChangedListener
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import androidx.core.content.edit

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
    private var mLocation = LatLng(30.507950, 114.413514)

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

    fun mapLocationInit(mapView: MapView) {
        mapView.map.setLocationSource(this)
        mLocationClient.setLocationListener { aMapLocation ->
            if (aMapLocation!!.errorCode == 0) {
                mListener!!.onLocationChanged(aMapLocation)
                mLocation = LatLng(aMapLocation.latitude, aMapLocation.longitude)
                saveLastKnownLocation(mLocation, aMapLocation.cityCode)
            }
        }
    }

    fun moveToLocation(mapView: MapView) {
        try {
            mapView.moveMap(getLastKnownLocation())
            mapView.moveMap(mLocation)
            mapView.moveMap(MapUtil.locationToLatlng(mapView.map.myLocation))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun navigate(context: Context, latLng: LatLng, mapView: MapView) {
        MapRouteSearchUtil(
            mapView = mapView,
            context = context,
            returnMsg = { MapUtil.showMsg(it, context) }).startRouteSearch(
            mStartPoint = mLocation, mEndPoint = latLng
        )
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