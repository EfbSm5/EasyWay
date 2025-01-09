package com.efbsm5.easyway.page

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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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

@Composable
fun EasyWay() {
    val navControl = rememberNavController()
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .imePadding()
            ) {
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
            HighlightButton(navController = navControl)
        }
    }
}

@Composable
fun HighlightButton(navController: NavController) {
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