package com.efbsm5.easyway.ui

import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.alibaba.idst.nui.BuildConfig
import com.amap.api.maps.AMap
import com.amap.api.maps.AMap.MAP_TYPE_NIGHT
import com.amap.api.maps.AMap.MAP_TYPE_NORMAL
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle
import com.efbsm5.easyway.data.models.assistModel.UpdateInfo
import com.efbsm5.easyway.data.network.HttpClient
import com.efbsm5.easyway.ui.page.communityPage.CommunityPage
import com.efbsm5.easyway.ui.page.homepage.HomePage
import com.efbsm5.easyway.ui.page.MapPage
import com.efbsm5.easyway.ui.theme.isDarkTheme
import com.efbsm5.easyway.viewmodel.ViewModelFactory
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel

private const val TAG = "Nav"

@Composable
fun EasyWay() {
    val navControl = rememberNavController()
    val context = LocalContext.current
    val mapPageViewModel = viewModel<MapPageViewModel>(factory = ViewModelFactory(context))
    val homePageViewModel = viewModel<HomePageViewModel>(factory = ViewModelFactory(context))
    val mapView = MapView(context, AMapOptions().compassEnabled(true))
    mapPageViewModel.drawPointsAndInitLocation(mapView)
    CheckUpdateUI(context = context)
    AppSurface(
        navController = navControl,
        mapPageViewModel = mapPageViewModel,
        homePageViewModel = homePageViewModel,
        mapView = mapView,
    )
}

@Composable
fun AppSurface(
    navController: NavHostController,
    mapPageViewModel: MapPageViewModel,
    homePageViewModel: HomePageViewModel,
    mapView: MapView,
) {
    Scaffold(bottomBar = { HighlightButton(navController = navController) }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(), factory = { mapView })
            NavHost(navController = navController, startDestination = "MapPage") {
                composable("MapPage") {
                    mapView.visibility = View.VISIBLE
                    MapPage(
                        viewModel = mapPageViewModel, mapView = mapView,
                    )
                }
                composable("Community") {
                    mapView.visibility = View.GONE
                    CommunityPage()
                }
                composable("home") {
                    mapView.visibility = View.GONE
                    HomePage(homePageViewModel)
                }
            }
        }
    }
    MapLifecycle(
        mapView = mapView,
        onPoiClick = mapPageViewModel.onPoiClick,
        onMapClick = mapPageViewModel.onMapClick,
        onMarkerClick = mapPageViewModel.onMarkerClick,
    )
}

@Composable
private fun HighlightButton(navController: NavController) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val direction = listOf(
        "MapPage", "Community", "home"
    )
    val buttons = listOf("首页", "社区", "我的")
    val icons = listOf(
        Icons.Default.Place,
        Icons.Default.AccountBox,
        Icons.Default.Home,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    LaunchedEffect(navBackStackEntry) {
        navBackStackEntry?.destination?.route?.let { route ->
            selectedIndex = direction.indexOf(route).takeIf { it != -1 } ?: 0
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(color = MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(100.dp)
                    .background(
                        color = backgroundColor, shape = RoundedCornerShape(90)
                    )
                    .clip(shape = RoundedCornerShape(90))
                    .clickable {
                        if (selectedIndex != index) {
                            selectedIndex = index
                            navController.navigate(direction[index])
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

@Composable
fun MapLifecycle(
    mapView: MapView,
    onMapClick: AMap.OnMapClickListener,
    onPoiClick: AMap.OnPOIClickListener,
    onMarkerClick: AMap.OnMarkerClickListener
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver(onResume = {
            mapView.map.apply {
                mapType = if (isDarkTheme(context)) MAP_TYPE_NIGHT else MAP_TYPE_NORMAL
                isMyLocationEnabled = true
                myLocationStyle = MyLocationStyle().interval(2000)
                    .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE)
                showMapText(true)
                uiSettings.apply {
                    isMyLocationButtonEnabled = true
                    zoomPosition = AMapOptions.ZOOM_POSITION_RIGHT_CENTER
                }
                setOnMapClickListener(onMapClick)
                setOnPOIClickListener(onPoiClick)
                setOnMarkerClickListener(onMarkerClick)
            }
        }, onPause = {
            mapView.map.apply {
                removeOnMapClickListener(onMapClick)
                removeOnPOIClickListener(onPoiClick)
                removeOnMarkerClickListener(onMarkerClick)
            }
        })
        val callbacks = mapView.componentCallbacks()
        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)
        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
            Log.e(TAG, "MapLifecycle:      remove")
            context.unregisterComponentCallbacks(callbacks)
        }
    }
}

private fun MapView.lifecycleObserver(
    onResume: () -> Unit, onPause: () -> Unit,
): LifecycleEventObserver = LifecycleEventObserver { _, event ->
    when (event) {
        Lifecycle.Event.ON_CREATE -> {
            this.onCreate(Bundle())
            Log.e(TAG, "lifecycleObserver:     oncreate view")
        }

        Lifecycle.Event.ON_RESUME -> {
            onResume()
            this.onResume()
            Log.e(TAG, "lifecycleObserver:           onresume view")
        }

        Lifecycle.Event.ON_PAUSE -> {
            onPause()
            this.onPause()
            Log.e(TAG, "lifecycleObserver:                on pause view")
        }

        Lifecycle.Event.ON_DESTROY -> {
            this.onDestroy()
            Log.e(TAG, "lifecycleObserver:            on destory view")
        } // 销毁地图
        else -> {}
    }
}

private fun MapView.componentCallbacks(): ComponentCallbacks = object : ComponentCallbacks {
    override fun onConfigurationChanged(config: Configuration) {}

    @Deprecated("Deprecated in Java", ReplaceWith("this@componentCallbacks.onLowMemory()"))
    override fun onLowMemory() {
        this@componentCallbacks.onLowMemory()
    }
}

@Composable
fun CheckUpdateUI(context: Context) {
    var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }
    LaunchedEffect(Unit) {
        HttpClient().checkForUpdate { info ->
            if (info != null && shouldUpdate(BuildConfig.VERSION_CODE, info.versionCode)) {
                updateInfo = info
            }
        }

    }
    updateInfo?.let {
        UpdateDialog(context, it) {
            updateInfo = if (it) null
            else null
        }
    }
}


@Composable
fun UpdateDialog(context: Context, updateInfo: UpdateInfo, callback: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { callback(false) },
        title = { Text(text = "发现新版本 ${updateInfo.versionName}") },
        text = { Text(text = updateInfo.updateMessage) },
        confirmButton = {
            Button(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, updateInfo.apkUrl.toUri())
                context.startActivity(intent)
                callback(true)
            }) {
                Text("立即更新")
            }
        },
        dismissButton = {
            Button(onClick = { callback(false) }) {
                Text("稍后再说")
            }
        })
}

private fun shouldUpdate(currentVersionCode: Int, latestVersionCode: Int): Boolean {
    return latestVersionCode > currentVersionCode
}