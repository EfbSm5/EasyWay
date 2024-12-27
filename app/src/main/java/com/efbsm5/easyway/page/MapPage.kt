package com.efbsm5.easyway.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.efbsm5.easyway.components.AddAndLocateButton
import com.efbsm5.easyway.components.CommentCard
import com.efbsm5.easyway.components.FunctionCard
import com.efbsm5.easyway.components.NewPlaceCard
import com.efbsm5.easyway.components.NewPointCard
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
    var content: Screen by remember { mutableStateOf(Screen.IconCard) }
    var iconText by remember { mutableStateOf("") }
    MapContent(
        mapView = mapView,
        onAdd = { isAdding = true },
        onLocate = { mapController.onLocate(mapView = mapView) },
        content = {
            when (content) {
                Screen.Comment -> CommentCard()
                Screen.IconCard -> FunctionCard(text = searchBarText,
                    onclick = {},
                    onTextChange = { searchBarText = it })
                Screen.NewPoint -> NewPointCard()
                Screen.Places -> NewPlaceCard()
            }
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapContent(
    mapView: MapView, onAdd: () -> Unit, onLocate: () -> Unit, content: @Composable () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(
        scaffoldState = scaffoldState, sheetContent = {
            content()
        }, sheetPeekHeight = 120.dp
    ) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView })
        AddAndLocateButton({ onAdd() }, { onLocate() })
    }
}

sealed interface Screen {
    data object IconCard : Screen
    data object NewPoint : Screen
    data object Places : Screen
    data object Comment : Screen
}


