package com.efbsm5.easyway

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.amap.api.maps.MapsInitializer
import com.amap.apis.utils.core.api.AMapUtilCoreApi
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.viewmodel.appModule
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


@SuppressLint("StaticFieldLeak")
object AppUtils {
    private lateinit var context: Context

    fun getContext(): Context {
        return context
    }

    fun init(context: Context) {
        this.context = context
        startKoin {
            androidContext(context)
            modules(appModule)
        }
        setUser()
        handlePrivacy()
    }

    private fun setUser() {
        UserManager.userId = 100
    }

    private fun handlePrivacy() {
        MapsInitializer.updatePrivacyShow(getContext(), true, true)
        MapsInitializer.updatePrivacyAgree(getContext(), true)
        AMapUtilCoreApi.setCollectInfoEnable(true)
    }

}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermission() {
    val state = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    state.launchPermissionRequest()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })
}

