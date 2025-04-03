package com.efbsm5.easyway.data.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface HttpInterface {
    @GET("{path}")
    fun <E> getData(@Path("path") modelname: String): Call<List<E>>
}