package com.efbsm5.easyway.map

import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.MapView

private const val TAG = "MapLifeCycle"

@Composable
fun MapLifecycle(
    mapView: MapView, onResume: () -> Unit = {}, onPause: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val previousState = remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver =
            mapView.lifecycleObserver(previousState = previousState, onResume, onPause)
        val callbacks = mapView.componentCallbacks()
        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)
        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
            Log.e(TAG, "MapLifecycle:      remove")
            context.unregisterComponentCallbacks(callbacks)
        }
    }
    DisposableEffect(mapView) {
        onDispose {
            mapView.onDestroy()
            mapView.removeAllViews()
        }
    }
}

private fun MapView.lifecycleObserver(
    previousState: MutableState<Lifecycle.Event>, onPause: () -> Unit, onResume: () -> Unit
): LifecycleEventObserver = LifecycleEventObserver { _, event ->
    when (event) {
        Lifecycle.Event.ON_CREATE -> {
            if (previousState.value != Lifecycle.Event.ON_STOP) {
                this.onCreate(Bundle())
                Log.e(TAG, "lifecycleObserver:create view")
            }
        }

        Lifecycle.Event.ON_RESUME -> {
            this.onResume()
            onResume
            Log.e(TAG, "lifecycleObserver:resume view")
        }

        Lifecycle.Event.ON_PAUSE -> {
            this.onPause()
            onPause
            Log.e(TAG, "lifecycleObserver:pause view")
        }

        Lifecycle.Event.ON_DESTROY -> {
            this.onDestroy()
            Log.e(TAG, "lifecycleObserver:destroy view")
        }

        else -> {}
    }
    previousState.value = event
}

private fun MapView.componentCallbacks(): ComponentCallbacks = object : ComponentCallbacks {
    override fun onConfigurationChanged(config: Configuration) {}

    @Deprecated("Deprecated in Java", ReplaceWith("this@componentCallbacks.onLowMemory()"))
    override fun onLowMemory() {
        this@componentCallbacks.onLowMemory()
        this.onLowMemory()
    }
}
