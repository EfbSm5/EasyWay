package com.efbsm5.easyway.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.efbsm5.easyway.components.AddAndLocateButton
import com.efbsm5.easyway.components.IconGrid
import com.efbsm5.easyway.components.NewPointCard
import com.efbsm5.easyway.components.SearchBar
import com.efbsm5.easyway.ultis.AppContext.context
import com.efbsm5.easyway.ultis.MapController
import com.efbsm5.easyway.ultis.MapUtil.showMsg


@Composable
fun MapPage() {
    val mapView = MapView(
        context, AMapOptions().compassEnabled(true)
    )
    val mapController = MapController(onPoiClick = { showMsg(it!!.name) },
        onMapClick = { showMsg(it!!.latitude.toString()) },
        onMarkerClick = { showMsg(it!!.id) })
    mapController.MapLifecycle(mapView)
    var searchBarText by remember { mutableStateOf("") }
    var isAdding by remember { mutableStateOf(false) }
    MapContent(
        mapView = mapView,
        onclick = {},
        text = searchBarText,
        onTextChange = { searchBarText = it },
        onAdd = { isAdding = true },
        onLocate = { mapController.onLocate(mapView = mapView) },
        isAdding = isAdding
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapContent(
    mapView: MapView,
    text: String,
    isAdding: Boolean,
    onclick: (String) -> Unit,
    onTextChange: (String) -> Unit,
    onAdd: () -> Unit,
    onLocate: () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        scaffoldState = scaffoldState, sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .heightIn(max = 350.dp)
                    .padding(16.dp)
            ) {
                SearchBar(text = text) { onTextChange(it) }
                Spacer(Modifier.height(10.dp))
                IconGrid { onclick(it) }
            }
        }, sheetPeekHeight = 120.dp
    ) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView })
        AddAndLocateButton({ onAdd() }, { onLocate() })
        if (isAdding) {
            NewPointCard()
        }
    }
}




