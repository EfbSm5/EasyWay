package com.efbsm5.easyway.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun DraggableBox(boxHeight: Dp, onChangeHeight: (Dp) -> Unit, content: @Composable () -> Unit) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    var offsetHeight by remember { mutableFloatStateOf(0f) }
    val density = LocalDensity.current
    val screenHeightPx = with(density) { screenHeight.toPx() }
    val initialBoxHeight = 100.dp
    val initialBoxHeightPx = with(density) { initialBoxHeight.toPx() }
    Box(
        modifier = Modifier
            .height(boxHeight)
            .fillMaxWidth()
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .draggable(orientation = Orientation.Vertical, state = rememberDraggableState { delta ->
                offsetHeight = (offsetHeight - delta).coerceIn(0f, screenHeightPx)
                onChangeHeight(
                    with(density) { (initialBoxHeightPx + offsetHeight).toDp() }.coerceIn(
                        100.dp, screenHeight
                    )
                )
            })
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = Color.Transparent,
                )
        ) {
            Spacer(
                Modifier
                    .height(10.dp)
                    .alpha(0.5f)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(8.dp)
                    .background(
                        color = Color.White, shape = RoundedCornerShape(2.dp)
                    )
                    .alpha(0.5f)
            )
            Spacer(
                Modifier
                    .height(10.dp)
                    .alpha(0.5f)
            )
            content()
        }
    }
}