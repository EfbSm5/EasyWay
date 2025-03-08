package com.efbsm5.easyway.viewmodel.componentsViewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.map.MapPoiSearchUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FunctionCardViewModel(context: Context) : ViewModel() {
    private val _poiList = MutableStateFlow(emptyList<PoiItemV2>())
    val poiList: StateFlow<List<PoiItemV2>> = _poiList
    private val searchController = MapPoiSearchUtil(
        context = context,
        onPoiSearched = { _poiList.value = it.toList() },
    )

    fun searchForPoi(string: String) {
        searchController.searchForPoi(string)
    }
}