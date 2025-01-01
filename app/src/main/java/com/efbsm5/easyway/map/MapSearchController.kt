package com.efbsm5.easyway.map

import android.content.Context
import com.amap.api.services.core.AMapException
import com.amap.api.services.core.PoiItemV2
import com.amap.api.services.core.ServiceSettings
import com.amap.api.services.poisearch.PoiResultV2
import com.amap.api.services.poisearch.PoiSearchV2

class MapSearchController(
    private val context: Context,
    val onPoiSearched: (ArrayList<PoiItemV2>) -> Unit,
) : PoiSearchV2.OnPoiSearchListener {
    fun searchForPoi(keyword: String) {
        Thread {
            ServiceSettings.updatePrivacyShow(context, true, true)
            ServiceSettings.updatePrivacyAgree(context, true)
            val query: PoiSearchV2.Query = PoiSearchV2.Query(keyword, "", "027")
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
        onPoiSearched(p0!!.pois)
    }

    override fun onPoiItemSearched(p0: PoiItemV2?, p1: Int) {
    }
}


