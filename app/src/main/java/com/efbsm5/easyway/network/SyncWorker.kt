package com.efbsm5.easyway.network

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.efbsm5.easyway.database.Repository

class SyncWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    private val repository = Repository(context)
    override fun doWork(): Result {
        return try {
            repository.syncData()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}