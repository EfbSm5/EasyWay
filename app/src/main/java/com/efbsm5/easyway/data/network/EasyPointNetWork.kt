package com.efbsm5.easyway.data.network

import android.util.Log
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.models.ModelNames
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.models.assistModel.EasyPointSimplify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object EasyPointNetWork {
    private val httpService = ServiceCreator.create<HttpService>()
    private suspend fun <T> GetData(modelNames: ModelNames): List<T> {
        val data = when (modelNames) {
            ModelNames.DynamicPosts -> httpService.getData<DynamicPost>(modelNames.replacePath())
                .await()

            ModelNames.Users -> httpService.getData<User>(modelNames.replacePath()).await()
            ModelNames.Comments -> httpService.getData<Comment>(modelNames.replacePath()).await()
            ModelNames.EasyPoints -> httpService.getData<EasyPoint>(modelNames.replacePath())
                .await()

            ModelNames.EasyPointSimplify -> httpService.getData<EasyPointSimplify>(modelNames.replacePath())
                .await()
        }
        return data as List<T>
    }

    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private suspend fun start(block: () -> Unit) {
        try {
            withContext(Dispatchers.IO) {
                block()
            }
        } catch (e: Exception) {
            Log.e("httpclient", "start: $e.message")
        }
    }

}