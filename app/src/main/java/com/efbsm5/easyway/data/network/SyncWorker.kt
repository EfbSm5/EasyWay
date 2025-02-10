package com.efbsm5.easyway.data.network

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class SyncWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val intentRepository = IntentRepository(context)
    override fun doWork(): Result {
        return try {
            intentRepository.syncData()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}