package com.efbsm5.easyway.map

import android.util.Log
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.AMapException
import com.amap.api.services.route.BusRouteResult
import com.amap.api.services.route.DriveRouteResult
import com.amap.api.services.route.RideRouteResult
import com.amap.api.services.route.RouteSearch
import com.amap.api.services.route.WalkRouteResult
import com.efbsm5.easyway.Myapplication
import com.efbsm5.easyway.R
import com.efbsm5.easyway.map.MapUtil.showMsg
import com.efbsm5.easyway.map.overlay.AMapServicesUtil.convertToLatLonPoint
import com.efbsm5.easyway.map.overlay.WalkRouteOverlay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "MapRouteSearchUtil"

fun startRouteSearch(
    mEndPoint: LatLng,
    mapView: MapView,
    locationSaver: LocationSaver,
    callBack: (Boolean) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val routeSearch = RouteSearch(Myapplication.getContext())
            routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {
                override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {}
                override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {}
                override fun onWalkRouteSearched(walkRouteResult: WalkRouteResult?, p1: Int) {
                    if (p1 != AMapException.CODE_AMAP_SUCCESS) {
                        Log.e(TAG, "onWalkRouteSearched: $p1")
                        showMsg("出错了")
                        callBack(false)
                        return
                    }
                    if (walkRouteResult?.paths == null) {
                        showMsg("没有搜索到相关数据")
                        callBack(false)
                        return
                    }
                    if (walkRouteResult.paths.isEmpty()) {
                        showMsg("无路线")
                        callBack(false)
                        return
                    }
                    val walkPath = walkRouteResult.paths[0] ?: return
                    val walkRouteOverlay = WalkRouteOverlay(
                        Myapplication.getContext(),
                        mapView.map,
                        walkPath,
                        walkRouteResult.startPos,
                        walkRouteResult.targetPos
                    )
                    walkRouteOverlay.removeFromMap()
                    walkRouteOverlay.addToMap()
                    walkRouteOverlay.zoomToSpan()
                    mapView.map.apply {
                        clear()
                        addMarker(
                            MarkerOptions().position(locationSaver.location)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start))
                        )
                        addMarker(
                            MarkerOptions().position(mEndPoint)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end))
                        )
                    }
                    callBack(true)
                }

                override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {}

            })

            val query = RouteSearch.WalkRouteQuery(
                RouteSearch.FromAndTo(
                    convertToLatLonPoint(locationSaver.location), convertToLatLonPoint(mEndPoint)
                ), RouteSearch.WalkDefault
            )
            routeSearch.calculateWalkRouteAsyn(query)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
