package com.efbsm5.easyway.map

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.core.ServiceSettings
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2

private const val TAG = "MapPoiSearchUtil"

class MapPoiSearchUtil(
    private val context: Context, val onPoiSearched: (ArrayList<PoiItemV2>) -> Unit
) : PoiSearchV2.OnPoiSearchListener {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MapPreferences", Context.MODE_PRIVATE)

    fun searchForPoi(keyword: String) {
        Thread {
            val cityCode = sharedPreferences.getString("citycode", "027")
            ServiceSettings.updatePrivacyShow(context, true, true)
            ServiceSettings.updatePrivacyAgree(context, true)
            val query: PoiSearchV2.Query = PoiSearchV2.Query(keyword, "", cityCode)
            query.pageSize = 5
            query.pageNum = 1
            try {
                val poiSearch = PoiSearchV2(context, query)
                poiSearch.setOnPoiSearchListener(this)
                poiSearch.searchPOIAsyn()
            } catch (e: AMapException) {
                throw RuntimeException(e)
            }
        }.start()
    }

    override fun onPoiSearched(p0: PoiResultV2?, p1: Int) {
        p0?.pois?.let { onPoiSearched(it) }
        Log.d(TAG, "onPoiSearched: ${p0?.pois.toString()}")
    }

    override fun onPoiItemSearched(p0: PoiItemV2?, p1: Int) {
    }

}

