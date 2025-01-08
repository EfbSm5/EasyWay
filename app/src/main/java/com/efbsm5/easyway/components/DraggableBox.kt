package com.efbsm5.easyway.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
                color = MaterialTheme.colorScheme.background,
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
    ) {
        content()
    }
}