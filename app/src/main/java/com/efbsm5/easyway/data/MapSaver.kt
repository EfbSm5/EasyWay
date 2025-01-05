package com.efbsm5.easyway.data

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amap.api.maps.MapView
import com.efbsm5.easyway.viewmodel.PointsViewModel
import com.efbsm5.easyway.viewmodel.PointsViewModelFactory

object MapSaver {
    lateinit var mapView: MapView
    var points: List<EasyPointSimplify>? = null

}