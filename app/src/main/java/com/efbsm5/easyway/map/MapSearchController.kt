package com.efbsm5.easyway.map

import android.content.Context
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.core.ServiceSettings
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2

class MapSearchController(
    val onPoiSearched: (ArrayList<PoiItemV2>) -> Unit,
) : PoiSearchV2.OnPoiSearchListener {
    private var cityCode = "027"
    fun searchForPoi(keyword: String, context: Context, pageSize: Int, pageNum: Int) {
        Thread {
            val sharedPreferences =
                context.getSharedPreferences("MapPreferences", Context.MODE_PRIVATE)
            sharedPreferences.getString("citycode", cityCode)
            ServiceSettings.updatePrivacyShow(context, true, true)
            ServiceSettings.updatePrivacyAgree(context, true)
            val query: PoiSearchV2.Query = PoiSearchV2.Query(keyword, "", cityCode)
            query.pageSize = pageSize
            query.pageNum = pageNum
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
        onPoiSearched(p0!!.pois)
    }

    override fun onPoiItemSearched(p0: PoiItemV2?, p1: Int) {
    }
}


