package com.efbsm5.easyway.map

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.amap.api.maps.MapView
import com.efbsm5.easyway.map.MapSaver.mapView

private const val TAG = "MapLifeCycle"

class MapLifeCycle {

    @Composable
    fun MapLifecycle(context: Context) {
        val lifecycle = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
        DisposableEffect(context, lifecycle, this) {
            val mapLifecycleObserver = lifecycleObserver(mapView)
            val callbacks = mapView.componentCallbacks()
            lifecycle.addObserver(mapLifecycleObserver)
            context.registerComponentCallbacks(callbacks)
            onDispose {
                lifecycle.removeObserver(mapLifecycleObserver)
                context.unregisterComponentCallbacks(callbacks)
            }
        }
    }

    private fun lifecycleObserver(mapView: MapView): LifecycleEventObserver =
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    mapView.onCreate(Bundle())
                    Log.e(TAG, "lifecycleObserver:     oncreate view")
                }

                Lifecycle.Event.ON_RESUME -> {
                    mapView.onResume()
                    Log.e(TAG, "lifecycleObserver:           onresume view")
                }

                Lifecycle.Event.ON_PAUSE -> {
                    mapView.onPause()
                    Log.e(TAG, "lifecycleObserver:                on pause view")
                }  // 暂停地图的绘制
                Lifecycle.Event.ON_DESTROY -> {
                    mapView.onDestroy()
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


}