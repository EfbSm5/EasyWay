package com.efbsm5.easyway.map

import android.content.Context
import android.util.Log
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.core.ServiceSettings
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2

private const val TAG = "MapPoiSearchUtil"


fun searchForPoi(
    keyword: String, context: Context, onPoiSearched: (ArrayList<PoiItemV2>) -> Unit
) {
    ServiceSettings.updatePrivacyShow(context, true, true)
    ServiceSettings.updatePrivacyAgree(context, true)
    val query: PoiSearchV2.Query = PoiSearchV2.Query(keyword, "", LocationSaver.cityCode)
    query.pageSize = 5
    query.pageNum = 1
    try {
        val poiSearch = PoiSearchV2(context, query)
        poiSearch.setOnPoiSearchListener(object : PoiSearchV2.OnPoiSearchListener {
            override fun onPoiSearched(p0: PoiResultV2?, p1: Int) {
                p0?.pois?.let { onPoiSearched(it) }
                Log.d(TAG, "onPoiSearched: ${p0?.pois.toString()}")
            }

            override fun onPoiItemSearched(p0: PoiItemV2?, p1: Int) {
            }
        })
        poiSearch.searchPOIAsyn()
    } catch (e: AMapException) {
        throw RuntimeException(e)
    }
}


