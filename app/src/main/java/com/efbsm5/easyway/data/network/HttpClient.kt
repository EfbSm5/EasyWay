package com.efbsm5.easyway.data.network

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.efbsm5.easyway.BuildConfig
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.database.UriTypeAdapter
import com.efbsm5.easyway.data.models.ModelNames
import com.efbsm5.easyway.data.models.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.jvm.java


object HttpClient {
    private val gson: Gson =
        GsonBuilder().registerTypeAdapter(Uri::class.java, UriTypeAdapter()).create()
    val placeService: HttpService by lazy {
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(HttpService::class.java)
    }


    private fun uriToFile(context: Context, uri: Uri): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File(context.cacheDir, "temp_image")
        val outputStream: OutputStream = FileOutputStream(tempFile)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }

    fun uploadImage(context: Context, uri: Uri, callback: (Uri?) -> Unit) {
        val file = uriToFile(context, uri)
        val mediaType = "image/jpeg".toMediaType()
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("photo", file.name, file.asRequestBody(mediaType)).build()
        val request = Request.Builder().url("$baseUrl/upload").post(requestBody).build()
        client.newCall(request).enqueue(getCallback { json ->
            var url: Uri? = null
            if (json != null) {
                val json = JSONObject(json)
                url = json.getString("url").toUri()
            }
            callback(url)
        })
    }

    fun uploadData(data: Any, callback: (Boolean) -> Unit) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = gson.toJson(data).toRequestBody(mediaType)
        var uploadType = when (data) {
            is User -> ModelNames.Users
            is Comment -> ModelNames.Comments
            is DynamicPost -> ModelNames.DynamicPosts
            is EasyPoint -> ModelNames.EasyPoints
            else -> throw IllegalArgumentException("unsupported data")
        }
        val request = Request.Builder().url("$baseUrl:5000/$uploadType").post(requestBody).build()

        client.newCall(request).enqueue(getCallback { })
    }


}
