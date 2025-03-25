package com.efbsm5.easyway.data.network

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Path


interface HttpService {
    @GET("{path}")
    fun <E> getData(@Path("path") modelname: String): Call<List<E>>
    val request = Request.Builder().url("$baseUrl/checkUpdate").build()

}