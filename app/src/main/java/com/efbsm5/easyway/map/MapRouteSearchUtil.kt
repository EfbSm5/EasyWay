package com.efbsm5.easyway.map

import android.content.Context
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
import com.efbsm5.easyway.R
import com.efbsm5.easyway.map.MapUtil.showMsg
import com.efbsm5.easyway.map.overlay.AMapServicesUtil.convertToLatLonPoint
import com.efbsm5.easyway.map.overlay.WalkRouteOverlay


fun startRouteSearch(
    mEndPoint: LatLng,
    mapView: MapView,
    context: Context,
) {
    Thread {
        try {
            val routeSearch = RouteSearch(context)
            val locationSaver = LocationSaver(context)
            routeSearch.setRouteSearchListener(object : RouteSearch.OnRouteSearchListener {

                override fun onBusRouteSearched(p0: BusRouteResult?, p1: Int) {}
                override fun onDriveRouteSearched(p0: DriveRouteResult?, p1: Int) {}
                override fun onWalkRouteSearched(walkRouteResult: WalkRouteResult?, p1: Int) {
                    if (p1 != AMapException.CODE_AMAP_SUCCESS) {
                        showMsg("出错了", context)
                        return
                    }
                    if (walkRouteResult?.paths == null) {
                        showMsg("没有搜索到相关数据", context)
                        return
                    }
                    if (walkRouteResult.paths.isEmpty()) {
                        showMsg("无路线", context)
                        return
                    }
                    val walkPath = walkRouteResult.paths[0] ?: return
                    val walkRouteOverlay = WalkRouteOverlay(
                        context,
                        mapView.map,
                        walkPath,
                        walkRouteResult.startPos,
                        walkRouteResult.targetPos
                    )
                    walkRouteOverlay.removeFromMap()
                    walkRouteOverlay.addToMap()
                    walkRouteOverlay.zoomToSpan()
                }

                override fun onRideRouteSearched(p0: RideRouteResult?, p1: Int) {}

            })
            mapView.map.clear()
            mapView.map.addMarker(
                MarkerOptions().position(locationSaver.location)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start))
            )
            mapView.map.addMarker(
                MarkerOptions().position(mEndPoint)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end))
            )
            val query = RouteSearch.WalkRouteQuery(
                RouteSearch.FromAndTo(
                    convertToLatLonPoint(locationSaver.location), convertToLatLonPoint(mEndPoint)
                ), RouteSearch.WalkDefault
            )
            routeSearch.calculateWalkRouteAsyn(query)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }.start()
}
