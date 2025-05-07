package com.efbsm5.easyway

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import com.amap.api.maps.MapsInitializer
import com.amap.apis.utils.core.api.AMapUtilCoreApi
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.viewmodel.appModule
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
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

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun RequestPermission(noGranted: (state: PermissionState) -> Unit) {
    val state = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    if (!state.status.isGranted) {
        noGranted
    }
}

