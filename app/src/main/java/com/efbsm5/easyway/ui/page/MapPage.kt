package com.efbsm5.easyway.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.amap.api.maps.AMap
import com.amap.api.maps.LocationSource
import com.efbsm5.easyway.map.GDMap
import com.efbsm5.easyway.map.LocationController
import com.efbsm5.easyway.map.LocationSaver
import com.efbsm5.easyway.map.MapState
import com.efbsm5.easyway.ui.components.mapcards.MapPageCard
import com.efbsm5.easyway.ui.components.mapcards.Screen
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(viewModel: MapPageViewModel) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val mapState by viewModel.mapState.collectAsState()
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.PartiallyExpanded)
    )
    val locationController =
        LocationController(LocationSaver(context)).apply { initLocation(context) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(state) {
        if (state != Screen.Function) scope.launch { sheetState.bottomSheetState.expand() }
    }
    MapScreen(
        state = state,
        onChangeScreen = viewModel::changeScreen,
        sheetState = sheetState,
        onMarkerClick = viewModel.onMarkerClick,
        onMapClick = viewModel.onMapClick,
        onPoiClick = viewModel.onPoiClick,
        mapState = mapState,
        onChangeMapState = viewModel::changeState,
        locationSource = locationController.locationSource,
        onAdd = { viewModel.changeScreen(Screen.NewPoint("新加标点")) })
    BackHandler(
        enabled = sheetState.bottomSheetState.currentValue == SheetValue.Expanded, onBack = {
            scope.launch { sheetState.bottomSheetState.partialExpand() }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapScreen(
    state: Screen,
    mapState: MapState,
    onChangeMapState: (MapState) -> Unit,
    onChangeScreen: (Screen) -> Unit,
    sheetState: BottomSheetScaffoldState,
    onMarkerClick: AMap.OnMarkerClickListener,
    onMapClick: AMap.OnMapClickListener,
    onPoiClick: AMap.OnPOIClickListener,
    locationSource: LocationSource,
    onAdd: () -> Unit
) {
    BottomSheetScaffold(sheetContent = {
        MapPageCard(
            content = state,
            onChangeScreen = onChangeScreen,
            onNavigate = { onChangeMapState(MapState.Route(it)) })
    }, scaffoldState = sheetState, sheetPeekHeight = 128.dp) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd) {
            IconButton(onAdd) {
                Icon(Icons.Default.Add, contentDescription = "add")
            }
        }
        GDMap(
            onMapClick = onMapClick,
            onPoiClick = onPoiClick,
            onMarkerClick = onMarkerClick,
            mapState = mapState,
            locationSource = locationSource
        )
    }
    BackHandler(enabled = state != Screen.Function, onBack = { onChangeScreen(Screen.Function) })
}
