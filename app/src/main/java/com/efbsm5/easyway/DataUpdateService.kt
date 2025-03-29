package com.efbsm5.easyway

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.efbsm5.easyway.data.network.SyncWorker
import java.util.concurrent.TimeUnit

class DataUpdateService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setupPeriodicSync()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
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
}