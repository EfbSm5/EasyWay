package com.efbsm5.easyway.viewmodel.pageViewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.map.MapPoiSearchUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SearchPageViewModel(context: Context) : ViewModel() {
    private val _text = MutableStateFlow("")
    private val _poiList = MutableStateFlow(emptyList<PoiItemV2>())
    val text: StateFlow<String> = _text
    val poiList: StateFlow<List<PoiItemV2>> = _poiList
    private val searchController = MapPoiSearchUtil(
        context = context,
        onPoiSearched = { _poiList.value = it.toList() },
    )

    fun changeText(string: String) {
        _text.value = string
    }

    fun searchForPoi() {
        searchController.searchForPoi(_text.value)
    }
}