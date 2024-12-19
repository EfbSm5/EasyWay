package com.efbsm5.easyway.page

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.services.core.LatLonPoint
import com.efbsm5.easyway.ultis.MapController


@Composable
fun MapPage(onNavigate: () -> Unit) {
    val context = LocalContext.current
    var currentState: State by remember { mutableStateOf(State.Map) }
    val mapView = MapView(
        context, AMapOptions()
    )
    val mapController = MapController {}
    mapController.MapLifecycle(mapView)
    MapPageSurface(
        mapView = mapView,
        currentState = currentState,
        onFactoryReset = {},
        onChangeState = { currentState = it },
        onChangeMap = {},
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPageSurface(
    mapView: MapView,
    currentState: State,
    onFactoryReset: () -> Unit,
    onChangeState: (State) -> Unit,
    onChangeMap: () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "这是一个无障碍地图APP") }, navigationIcon = {
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

    }) { paddingValues ->
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(paddingValues)) {
            MapContent(currentScreen = currentState, mapView = mapView) { onChangeState(it) }
        }
    }
}



@Composable
private fun Menu(onChangeMap: () -> Unit, onFactoryReset: () -> Unit) {
    DropdownMenuItem(text = { Text(text = "更换地图显示样式") }, onClick = {
        onChangeMap()
    })
    DropdownMenuItem(text = { Text(text = "重置所有") }, onClick = { onFactoryReset() })
}

@Composable
private fun MapContent(
    currentScreen: State, mapView: MapView, onChangeMap: (State) -> Unit
) {
    Crossfade(targetState = currentScreen, label = "") { screen ->
        when (screen) {
            State.Map -> {
                ShowMapScreen(mEndPoint = mEndPoint, mapView = mapView, toSearchScreen = {
                    onChangeMap(State.Search)
                }, toRouteScreen = {
                    mapTool.startRouteSearch(
                        mStartPoint = LatLonPoint(30.507964, 114.413512), mEndPoint = mEndPoint
                    )
                }, clear = {
                    mapView.map.clear()
                    initPoints(mapView, this@MainActivity)
                })
            }

            State.Search -> {
                ShowSearchScreen(poiList = pois,
                    toShowMapScreen = { currentScreen = State.Map },
                    searchForPoi = {
                        mapTool.searchForPoi(it)
                    },
                    onSelected = { mapTool.onSelected(it) })
            }

            State.Route -> {
                mapView.removeAllViews()
                mapTool.startRouteSearch(
                    mStartPoint = LatLonPoint(30.507964, 114.413512), mEndPoint = mEndPoint
                )
                RouteSearchScreen(mapView = mapView) { currentScreen = State.Map }
            }
        }
    }
}

override fun returnPoi(items: ArrayList<Markers>) {
    TODO("Not yet implemented")
}

override fun returnMsg(word: String) {
    TODO("Not yet implemented")
}

override fun returnPoint(point: LatLonPoint) {
    TODO("Not yet implemented")
}

override fun returnEndLocation(point: LatLonPoint, name: String) {
    TODO("Not yet implemented")
}

override fun returnNowLocation(point: LatLonPoint) {
    TODO("Not yet implemented")
}


interface State {
    data object Map : State
    data object Search : State
    data object Pois : State
}
