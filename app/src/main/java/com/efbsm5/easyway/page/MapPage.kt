package com.efbsm5.easyway.page

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.efbsm5.easyway.ultis.MapController

@Preview
@Composable
fun preview() {
    MapPage { }
}

@Composable
fun MapPage(onNavigate: () -> Unit) {
    val context = LocalContext.current
    var currentState: State by remember { mutableStateOf(State.Map) }
    val mapView = MapView(
        context, AMapOptions()
    )
    val mapController = MapController()
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

    }, bottomBar = {
        NavigationBar {
            Spacer(Modifier.weight(6f))
            Button(modifier = Modifier.weight(1f), onClick = {}) {
                Column() {
                    Icon(Icons.Default.Home, null)
                    Text()
                }
            }
        }
    }) { paddingValues ->
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(paddingValues)) {
            MapContent(currentState = currentState, mapView = mapView) { onChangeState(it) }
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
    currentState: State, mapView: MapView, onChangeMap: (State) -> Unit
) {
    AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView })
    Box(
        modifier = Modifier.fillMaxWidth().height(
            when (currentState) {
                State.Map -> 80.dp
                State.Search -> 300.dp
                State.Pois -> 300.dp
                else -> 80.dp
            }
        ).align(Alignment.BottomCenter).clip(
            RoundedCornerShape(
                topStart = cornerRadius.value, topEnd = cornerRadius.value
            )
        ).background(MaterialTheme.colorScheme.background).draggable(
            state = rememberDraggableState { deltaY ->
                if (deltaY < 0) isExpanded.value = true else if (deltaY > 0) isExpanded.value =
                    false
            }, orientation = Orientation.Vertical
        )
    ) {
        if (isExpanded.value) {
            // 展开时的内容
            ExpandedContent()
        } else {
            // 收起时的内容
            CollapsedContent()
        }
    }
}


interface State {
    data object Map : State
    data object Search : State
    data object Pois : State
}


@Composable
fun MapWithSearchBar() {
    // 状态变量
    val isExpanded = remember { mutableStateOf(false) } // 搜索栏是否展开
    val searchBarHeight = animateDpAsState(if (isExpanded.value) 300.dp else 80.dp, label = "")
    val cornerRadius = animateDpAsState(if (isExpanded.value) 0.dp else 16.dp, label = "")
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                MapView(context).apply {
                    onCreate(null) // 初始化高德地图
                }
            }, modifier = Modifier.fillMaxSize()
        )

        // 搜索栏
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(searchBarHeight.value)
                .align(Alignment.BottomCenter)
                .clip(
                    RoundedCornerShape(
                        topStart = cornerRadius.value, topEnd = cornerRadius.value
                    )
                )
                .background(MaterialTheme.colorScheme.background)
                .draggable(
                    state = rememberDraggableState { deltaY ->
                        if (deltaY < 0) isExpanded.value =
                            true else if (deltaY > 0) isExpanded.value = false
                    }, orientation = Orientation.Vertical
                )
        ) {
            if (isExpanded.value) {
                // 展开时的内容
                ExpandedContent()
            } else {
                // 收起时的内容
                CollapsedContent()
            }
        }
    }
}

@Composable
fun CollapsedContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "查找地点，无障碍设施", style = MaterialTheme.typography.body1)
    }
}

@Composable
fun ExpandedContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 搜索框
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "输入关键词搜索", style = MaterialTheme.typography.body1)
        }
        // 功能按钮网格

    }
}

