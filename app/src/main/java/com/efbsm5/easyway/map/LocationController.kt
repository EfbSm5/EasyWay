package com.efbsm5.easyway.map

import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.maps.LocationSource
import com.amap.api.maps.LocationSource.OnLocationChangedListener
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.Myapplication


class LocationController {
    private var mLocationClient: AMapLocationClient? = null
    private var mListener: OnLocationChangedListener? = null
    private var locationSource: LocationSource = object : LocationSource {
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

    fun getLocationSource(): LocationSource {
        return locationSource
    }

    init {
        initClient()
        mLocationClient!!.setLocationListener { aMapLocation ->
            if (aMapLocation!!.errorCode == 0) {
                mListener!!.onLocationChanged(aMapLocation)
                LocationSaver.location = LatLng(aMapLocation.latitude, aMapLocation.longitude)
                LocationSaver.locationDetail = aMapLocation.address
                LocationSaver.cityCode = aMapLocation.cityCode
            }
        }
    }

    private fun initClient() {
        mLocationClient = AMapLocationClient(Myapplication.getContext()).apply {
            setLocationOption(
                AMapLocationClientOption().setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
                    .setOnceLocation(true).setOnceLocationLatest(true).setNeedAddress(true)
                    .setHttpTimeOut(6000)
            )
        }
    }
}