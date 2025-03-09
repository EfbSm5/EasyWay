package com.efbsm5.easyway.viewmodel.componentsViewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.Repository.DataRepository
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.map.MapPoiSearchUtil
import com.efbsm5.easyway.map.MapUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FunctionCardViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private val _poiList = MutableStateFlow(emptyList<PoiItemV2>())
    private val _points = MutableStateFlow(emptyList<EasyPoint>())
    val points: StateFlow<List<EasyPoint>> = _points
    val poiList: StateFlow<List<PoiItemV2>> = _poiList
    private val searchUtil = MapPoiSearchUtil(
        context = context, onPoiSearched = { _poiList.value = it })

    fun search(string: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchUtil.searchForPoi(string)
            repository.getPointByName(string).collect {
                _points.value = it
            }
        }
    }

    fun navigate(context: Context, latLng: LatLng) {
        MapUtil.onNavigate(context, latLng)
    }

}

