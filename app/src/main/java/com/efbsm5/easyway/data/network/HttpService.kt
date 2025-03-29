package com.efbsm5.easyway.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface HttpService {
    @GET("{path}")
    fun <E> getData(@Path("path") modelname: String): Call<List<E>>
    val request = Request.Builder().url("$baseUrl/checkUpdate").build()

}