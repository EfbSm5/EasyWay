package com.efbsm5.easyway.ui

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.efbsm5.easyway.data.network.CheckUpdate
import com.efbsm5.easyway.ui.page.communityPage.CommunityPage
import com.efbsm5.easyway.ui.page.homepage.HomePage
import com.efbsm5.easyway.ui.page.MapPage
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.MapPageViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun EasyWay() {
    CheckUpdate()
    val navController = rememberNavController()
    val mapPageViewModel: MapPageViewModel = koinViewModel()
    val homePageViewModel: HomePageViewModel = koinViewModel()
    Scaffold(bottomBar = { HighlightButton(navController = navController) }) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            NavHost(navController = navController, startDestination = "MapPage") {
                composable("MapPage") {
                    MapPage(viewModel = mapPageViewModel)
                }
                composable("Community") {
                    CommunityPage()
                }
                composable("home") {
                    HomePage(homePageViewModel)
                }
            }
        }
    }
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


