package com.efbsm5.easyway.viewmodel.componentsViewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.models.assistModel.EasyPointSimplify
import com.efbsm5.easyway.map.MapPoiSearchUtil
import com.efbsm5.easyway.map.MapUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NewPlaceCardViewModel(context: Context) : ViewModel() {
    private val _selectedTab = MutableStateFlow(0)
    private val _points = MutableStateFlow(emptyList<EasyPointSimplify>())
    private val _poiList = MutableStateFlow(emptyList<PoiItemV2>())
    private val _showDialog = MutableStateFlow(false)
    val points: StateFlow<List<EasyPointSimplify>> = _points
    val selectedTab: StateFlow<Int> = _selectedTab
    var latLng: LatLng? = null
    var destination: LatLng? = null
    val poiList: StateFlow<List<PoiItemV2>> = _poiList
    val showDialog: StateFlow<Boolean> = _showDialog


    fun changeTab(int: Int) {
        _selectedTab.value = int
    }

    fun getLatlng(latLng: LatLng?) {
        this.latLng = latLng
    }

    fun search(string: String, context: Context) {
        val mapPoiSearchUtil = MapPoiSearchUtil(context = context,
            onPoiSearched = {},
            returnMsg = { MapUtil.showMsg(it, context) })
        mapPoiSearchUtil.searchForPoi(string)
    }

    fun showDialog(destination: LatLng) {
        this.destination = destination
        _showDialog.value = true
    }

    fun confirmDialog() {
        _showDialog.value = false
    }

    fun cancelDialog(context: Context) {
        _showDialog.value = false
        MapUtil.onNavigate(
            context = context,
            latLng = destination!!,
        )
    }
}