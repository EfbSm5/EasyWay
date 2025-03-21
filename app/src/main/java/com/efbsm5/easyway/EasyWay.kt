package com.efbsm5.easyway

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.efbsm5.easyway.ui.theme.EasyWayTheme
import com.amap.api.maps.MapsInitializer
import com.amap.apis.utils.core.api.AMapUtilCoreApi
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.network.SyncWorker
import com.efbsm5.easyway.ui.EasyWay
import com.efbsm5.easyway.viewmodel.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val tag = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        handlePermission()
        setupPeriodicSync()
        setUser()
        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }
        setContent {
            EasyWayTheme {
                EasyWay()
            }
        }
    }

    private fun handlePermission() {
        MapsInitializer.updatePrivacyShow(this, true, true)
        MapsInitializer.updatePrivacyAgree(this, true)
        AMapUtilCoreApi.setCollectInfoEnable(true)
        val requestPermission = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { result: Boolean ->
            Log.d(tag, "onCreate: $result")
            if (!result) {
                Toast.makeText(this, "请授予权限", Toast.LENGTH_LONG).show()
            }
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setupPeriodicSync() {
        val syncWorkRequest =
            PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS).setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
            ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SyncWork", ExistingPeriodicWorkPolicy.KEEP, syncWorkRequest
        )
    }

    private fun setUser() {
        val userManager = UserManager(context = this)
        userManager.userId = 100
    }
}