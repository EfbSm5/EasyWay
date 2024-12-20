package com.efbsm5.easyway.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.efbsm5.easyway.components.IconGrid
import com.efbsm5.easyway.components.Menu
import com.efbsm5.easyway.components.SearchBar
import com.efbsm5.easyway.ultis.MapController


@Composable
fun MapPage(onNavigate: () -> Unit) {
    val context = LocalContext.current
    val mapView = MapView(
        context, AMapOptions()
    )
    val mapController = MapController()
    mapController.MapLifecycle(mapView)
    MapPageSurface(mapView = mapView,
        onFactoryReset = {},
        onChangeMap = {},
        onNavigate = { onNavigate() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPageSurface(
    mapView: MapView, onFactoryReset: () -> Unit, onChangeMap: () -> Unit, onNavigate: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "EasyWay") }, navigationIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .wrapContentSize(Alignment.TopStart)
                ) {
                    Menu(onChangeMap = { onChangeMap() }, onFactoryReset = { onFactoryReset() })
                }
            })

        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(paddingValues)) {
            MapContent(mapView = mapView) { }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapContent(
    mapView: MapView, onclick: (String) -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = scaffoldState, sheetContent = {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                SearchBar()
                Spacer(modifier = Modifier.height(16.dp))
                IconGrid { onclick(it) }
            }
        }, sheetPeekHeight = 56.dp // 初始显示高度
    ) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView })

    }


}