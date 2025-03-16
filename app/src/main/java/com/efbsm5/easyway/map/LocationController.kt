package com.efbsm5.easyway.map

import android.content.Context
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.LocationSource
import com.amap.api.maps.LocationSource.OnLocationChangedListener
import com.amap.api.maps.model.LatLng


class LocationController(
    private val locationSaver: LocationSaver
) {
    private var mLocationClient: AMapLocationClient? = null
    private var mListener: OnLocationChangedListener? = null
    var locationSource: LocationSource = object : LocationSource {
        override fun activate(p0: OnLocationChangedListener?) {
            if (mListener == null) {
                mListener = p0
            }
            mLocationClient?.startLocation()
        }

        override fun deactivate() {
            mListener = null
            mLocationClient?.let {
                it.stopLocation()
                it.onDestroy()
            }
        }
    }

    fun initLocation(context: Context) {
        if (mLocationClient == null) {
            initClient(context = context)
        }
        mLocationClient!!.setLocationListener { aMapLocation ->
            if (aMapLocation!!.errorCode == 0) {
                mListener!!.onLocationChanged(aMapLocation)
                locationSaver.location = LatLng(aMapLocation.latitude, aMapLocation.longitude)
                locationSaver.locationDetail = aMapLocation.address
                locationSaver.cityCode = aMapLocation.cityCode
            }
        }
    }

    private fun initClient(context: Context) {
        mLocationClient = AMapLocationClient(context).apply {
            setLocationOption(
                AMapLocationClientOption().setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                    .setOnceLocation(true).setOnceLocationLatest(true).setNeedAddress(true)
                    .setHttpTimeOut(6000)
            )
        }
    }
}