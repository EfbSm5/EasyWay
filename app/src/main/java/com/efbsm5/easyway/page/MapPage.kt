package com.efbsm5.easyway.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.components.AddAndLocateButton
import com.efbsm5.easyway.components.CommentAndHistoryCard
import com.efbsm5.easyway.components.DraggableBox
import com.efbsm5.easyway.components.FunctionCard
import com.efbsm5.easyway.components.NewPlaceCard
import com.efbsm5.easyway.components.NewPointCard
import com.efbsm5.easyway.map.MapSaver.mapController
import com.efbsm5.easyway.map.MapSaver.mapView

private const val TAG = "MapPage"

@Composable
fun MapPage() {
    val context = LocalContext.current
    var content: Screen by remember { mutableStateOf(Screen.IconCard) }
    var state: MapState by remember { mutableStateOf(MapState.MapPage) }
    var boxHeight by remember { mutableStateOf(100.dp) }
    Box(modifier = Modifier.fillMaxSize()) {
        when (state) {
            MapState.CommunityPage -> {
                CommunityPage()
            }

            MapState.HomePage -> {
                HomePage()
            }

            MapState.MapPage -> {

            }
        }
        MapScreen(
            onAdd = { content = Screen.NewPoint(mapController.getLastKnownLocation()) },
            onLocate = { mapController.onLocate() },
            content = {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surfaceContainer)
                ) {
                    when (content) {
                        is Screen.Comment -> {
                            CommentAndHistoryCard(marker = (content as Screen.Comment).marker)
                        }

                        Screen.IconCard -> FunctionCard(onclick = {
                            content = Screen.Places(it)
                        })

                        is Screen.NewPoint -> NewPointCard((content as Screen.NewPoint).location)

                        is Screen.Places -> {
                            NewPlaceCard(
                                mapController.getLastKnownLocation()!!,
                                (content as Screen.Places).name,
                            )
                        }
                    }
                }
            },
            boxHeight = boxHeight,
            onChangeHeight = { boxHeight = it },
        )
        BackHandler(enabled = content != Screen.IconCard, onBack = { content = Screen.IconCard })

    }
    BottomAppBar { HighlightButton { state = it } }
}

@Composable
private fun MapScreen(
    onAdd: () -> Unit,
    onLocate: () -> Unit,
    boxHeight: Dp,
    onChangeHeight: (Dp) -> Unit,
    content: @Composable () -> Unit
) {
    DisposableEffect(Unit) {
        onDispose {
            mapView.onPause()
            Log.e(TAG, "MapScreen: on pause")
        }
    }
    AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView }, update = { view ->
//        if (view.parent != null) {
//            (view.parent as ViewGroup).removeView(view)
//        }
//        (view.parent as? ViewGroup)?.addView(view)
//        view.onResume()
    })
    AddAndLocateButton(onAdd = {
        onAdd()
    }, onLocate = { onLocate() }, bottomHeight = boxHeight
    )
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
    ) {
        DraggableBox(
            boxHeight = boxHeight, onChangeHeight = { onChangeHeight(it) }, content = content
        )
    }
}


sealed interface Screen {
    data object IconCard : Screen
    data class NewPoint(val location: LatLng?) : Screen
    data class Places(val name: String) : Screen
    data class Comment(val marker: Marker) : Screen
}

sealed interface MapState {
    data object MapPage : MapState
    data object CommunityPage : MapState
    data object HomePage : MapState
}

@Composable
private fun HighlightButton(onChangeState: (MapState) -> Unit) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val direction = listOf(
        MapState.MapPage, MapState.HomePage, MapState.CommunityPage
    )
    val buttons = listOf("首页", "社区", "我的")
    val icons = listOf(
        Icons.Default.Place,
        Icons.Default.AccountBox,
        Icons.Default.Home,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        buttons.forEachIndexed { index, text ->
            val backgroundColor by animateColorAsState(
                targetValue = if (selectedIndex == index) MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.2f
                ) else Color.Transparent, animationSpec = tween(durationMillis = 300), label = ""
            )
            val textColor by animateColorAsState(
                targetValue = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                animationSpec = tween(durationMillis = 300),
                label = ""
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(100.dp)
                    .background(
                        color = backgroundColor, shape = RoundedCornerShape(90)
                    )
                    .clip(shape = RoundedCornerShape(90))
                    .clickable {
                        if (selectedIndex != index) {
                            selectedIndex = index
                            onChangeState(direction[index])
                        }
                    }) {
                Icon(
                    imageVector = icons[index], contentDescription = text, tint = textColor
                )
                Text(
                    text = text, fontSize = 14.sp, color = textColor
                )
            }
        }
    }
}