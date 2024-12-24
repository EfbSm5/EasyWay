package com.efbsm5.easyway.page

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.AMap.MAP_TYPE_NIGHT
import com.amap.api.maps.AMap.MAP_TYPE_NORMAL
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.navi.view.nightmode.NightModeView
import com.efbsm5.easyway.components.IconGrid
import com.efbsm5.easyway.components.Menu
import com.efbsm5.easyway.components.SearchBar
import com.efbsm5.easyway.ultis.AppContext
import com.efbsm5.easyway.ultis.MapController


@Composable
fun MapPage(onNavigate: () -> Unit) {
    val context = LocalContext.current
    val mapView = MapView(
        context,
        AMapOptions().compassEnabled(true)
    )
    val mapController = MapController({ showMsg(it!!.name) },
        { showMsg(it!!.latitude.toString()) },
        { showMsg(it!!.id) })
    mapController.MapLifecycle(mapView)
    MapPageSurface(onFactoryReset = {}, onChangeMap = {}, onNavigate = { onNavigate() }, content = {
        MapContent(mapView = mapView,
            onclick = {},
            text = "",
            onTextChange = {},
            onAdd = {},
            onLocate = { mapController.onLocate(mapView = mapView) })
    })
}

fun showMsg(text: String) {
    Toast.makeText(AppContext.context, text, Toast.LENGTH_SHORT).show()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPageSurface(
    onFactoryReset: () -> Unit,
    onChangeMap: () -> Unit,
    onNavigate: () -> Unit,
    content: @Composable (() -> Unit),
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
                    Menu(onChangeMap = { onChangeMap() },
                        onFactoryReset = { onFactoryReset() },
                        onNavigate = { onNavigate() })
                }
            })

        },
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(paddingValues)) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapContent(
    mapView: MapView,
    onclick: (String) -> Unit,
    text: String,
    onTextChange: (String) -> Unit,
    onAdd: () -> Unit,
    onLocate:  () -> Unit
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    BottomSheetScaffold(scaffoldState = scaffoldState, sheetContent = {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)

        ) {
            SearchBar(text = text) { onTextChange(it) }
            Spacer(Modifier.height(10.dp))
            IconGrid { onclick(it) }
        }
    }, sheetPeekHeight = 120.dp, modifier = Modifier.alpha(0.5f)) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView })
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            FloatingActionButton(
                onClick = { onAdd() },
                modifier = Modifier.padding(bottom = 16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "新加无障碍点",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
            FloatingActionButton(
                onClick = { onLocate() },
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 120.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "定位",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        }
    }


}