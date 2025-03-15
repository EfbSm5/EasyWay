package com.efbsm5.easyway.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.ui.components.mapcards.MapPageCard
import com.efbsm5.easyway.ui.components.mapcards.Screen
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage(viewModel: MapPageViewModel, mapView: MapView) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val sheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(initialValue = SheetValue.PartiallyExpanded)
    )
    val scope = rememberCoroutineScope()
    LaunchedEffect(state) {
        if (state != Screen.Function) scope.launch { sheetState.bottomSheetState.expand() }
    }
    MapScreen(
        state = state,
        onChangeScreen = { viewModel.changeScreen(it) },
        navigate = { viewModel.navigate(context, it, mapView) },
        sheetState = sheetState,
        onAdd = {
            viewModel.changeScreen(
                Screen.NewPoint(label = "新增点位")
            )
        },
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
    onChangeScreen: (Screen) -> Unit,
    sheetState: BottomSheetScaffoldState,
    navigate: (LatLng) -> Unit,
    onAdd: () -> Unit
) {
    BottomSheetScaffold(sheetContent = {
        MapPageCard(
            content = state,
            onChangeScreen = { onChangeScreen(it) },
            onNavigate = navigate
        )
    }, scaffoldState = sheetState, sheetPeekHeight = 128.dp) {
        IconButton(
            onClick = onAdd, modifier = Modifier
                .size(50.dp)
                .alpha(0.7f)
                .clip(RoundedCornerShape(11))
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "新加无障碍点",
            )
        }
    }
    BackHandler(enabled = state != Screen.Function, onBack = { onChangeScreen(Screen.Function) })
}
