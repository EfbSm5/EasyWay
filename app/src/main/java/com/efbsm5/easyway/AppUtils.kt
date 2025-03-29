package com.efbsm5.easyway

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import androidx.compose.runtime.Composable
import com.amap.api.maps.MapsInitializer
import com.amap.apis.utils.core.api.AMapUtilCoreApi
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.viewmodel.appModule
import com.google.accompanist.permissions.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class Myapplication : Activity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context
        fun getContext(): Context {
            return context
        }
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        startKoin {
            androidContext(this@Myapplication)
            modules(appModule)
        }
        setUser()
        context = this.applicationContext
        handlePrivacy()
    }

    private fun setUser() {
        val userManager = UserManager(context = this)
        userManager.userId = 100
    }

    private fun handlePrivacy() {
        MapsInitializer.updatePrivacyShow(getContext(), true, true)
        MapsInitializer.updatePrivacyAgree(getContext(), true)
        AMapUtilCoreApi.setCollectInfoEnable(true)
    }

}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
fun RequestPermission() {
    val state = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    if (!state.status.isGranted) {
        requirePermission(state)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun requirePermission(state: PermissionState) {
    state.launchPermissionRequest()
}

