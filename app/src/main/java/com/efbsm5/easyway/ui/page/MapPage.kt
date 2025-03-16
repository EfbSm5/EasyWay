package com.efbsm5.easyway.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.amap.api.maps.AMap
import com.efbsm5.easyway.map.GDMap
import com.efbsm5.easyway.map.MapState
import com.efbsm5.easyway.ui.components.mapcards.MapPageCard
import com.efbsm5.easyway.ui.components.mapcards.Screen
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(viewModel: MapPageViewModel) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.PartiallyExpanded)
    )
    val points by viewModel.points.collectAsState()
    var mapState by remember { mutableStateOf<MapState>(MapState.Point(points)) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(state) {
        if (state != Screen.Function) scope.launch { sheetState.bottomSheetState.expand() }
    }
    LaunchedEffect(points) {
        mapState = MapState.Point(points)
    }
    MapScreen(
        state = state,
        onChangeScreen = { viewModel.changeScreen(it) },
        sheetState = sheetState,
        onMarkerClick = viewModel.onMarkerClick,
        onMapClick = viewModel.onMapClick,
        onPoiClick = viewModel.onPoiClick,
        mapState = mapState,
        onChangeMapState = { mapState = it },
    )
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
    onPoiClick: AMap.OnPOIClickListener
) {
    BottomSheetScaffold(sheetContent = {
        MapPageCard(
            content = state,
            onChangeScreen = { onChangeScreen(it) },
            onNavigate = { onChangeMapState(MapState.Route(it)) })
    }, scaffoldState = sheetState, sheetPeekHeight = 128.dp) {
        GDMap(
            onMapClick = onMapClick,
            onPoiClick = onPoiClick,
            onMarkerClick = onMarkerClick,
            mapState = mapState
        )
    }
    BackHandler(enabled = state != Screen.Function, onBack = { onChangeScreen(Screen.Function) })
}
