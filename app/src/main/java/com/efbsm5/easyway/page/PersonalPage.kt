package com.efbsm5.easyway.page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

@Composable
fun PersonalPage() {
    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        MyApp()
    }
}

@Composable
fun MyApp() {
    var isKeyboardVisible by remember { mutableStateOf(false) }

    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            val isVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            isKeyboardVisible = isVisible
            insets
        }
        onDispose {
            listener.let { ViewCompat.setOnApplyWindowInsetsListener(view, null) }
        }
    }
    Box(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(isKeyboardVisible.toString())
            TextField("", {})
        }

    }


}