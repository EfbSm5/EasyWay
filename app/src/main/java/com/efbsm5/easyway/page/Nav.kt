package com.efbsm5.easyway.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.efbsm5.easyway.data.MapSaver
import com.efbsm5.easyway.viewmodel.PointsViewModel
import com.efbsm5.easyway.viewmodel.PointsViewModelFactory

@Composable
fun EasyWay() {
    val context = LocalContext.current
    val navControl = rememberNavController()
    val map = MapSaver
    map.mapView = MapView(
        context, AMapOptions().compassEnabled(true)
    )
    val pointsViewModel: PointsViewModel = viewModel(factory = PointsViewModelFactory(context))
    map.points by pointsViewModel.points.collectAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                HighlightButtonExample(navController = navControl)
            }
        },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.surfaceContainer
    ) { paddingValues ->
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(paddingValues)) {
            NavHost(navController = navControl, startDestination = "MapPage") {
                composable("MapPage") {
                    MapPage()
                }
                composable("Community") {
                    CommunityPage()
                }
                composable("home") {
                    HomePage()
                }
            }
        }
    }
}

@Composable
fun HighlightButtonExample(navController: NavController) {
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        buttons.forEachIndexed { index, text ->
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width(100.dp)
                    .background(
                        color = if (selectedIndex == index) MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.2f
                        ) else Color.Transparent, shape = RoundedCornerShape(90)
                    )
                    .clip(shape = RoundedCornerShape(90))
                    .clickable {
                        if (selectedIndex != index) {
                            selectedIndex = index
                            navController.navigate(direction[index])
                        }
                    }) {
                Icon(
                    imageVector = icons[index],
                    contentDescription = text,
                    tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = if (selectedIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}