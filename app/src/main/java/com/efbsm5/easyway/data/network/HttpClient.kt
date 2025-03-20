package com.efbsm5.easyway.data.network

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.efbsm5.easyway.BuildConfig
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.database.UriTypeAdapter
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.models.assistModel.UpdateInfo
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.URL

private const val TAG = "HttpClient"

class HttpClient() {
    private val client = OkHttpClient()
    private val baseUrl = BuildConfig.BASE_URL
    private val gson: Gson =
        GsonBuilder().registerTypeAdapter(Uri::class.java, UriTypeAdapter()).create()

    fun getAllComments(callback: (List<Comment>?) -> Unit) {
        val request = Request.Builder().url("$baseUrl/comments").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val comments = gson.fromJson(responseData, Array<Comment>::class.java).toList()
                    callback(comments)
                } else {
                    callback(null)
                }
            }
        })
    }

    fun getAllUsers(callback: (List<User>?) -> Unit) {
        val request = Request.Builder().url("$baseUrl/users").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val users = gson.fromJson(responseData, Array<User>::class.java).toList()
                    callback(users)
                } else {
                    callback(null)
                }
            }
        })
    }

    fun getAllEasyPoints(callback: (List<EasyPoint>?) -> Unit) {
        val request = Request.Builder().url("$baseUrl/easypoints/full").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val EasyPoints =
                        gson.fromJson(responseData, Array<EasyPoint>::class.java).toList()
                    callback(EasyPoints)
                } else {
                    callback(null)
                }
            }
        })
    }

    fun getAllDynamicPosts(callback: (List<DynamicPost>?) -> Unit) {
        val request = Request.Builder().url("$baseUrl/dynamicposts").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val dynamicPosts =
                        gson.fromJson(responseData, Array<DynamicPost>::class.java).toList()
                    callback(dynamicPosts)
                } else {
                    callback(null)
                }
            }
        })
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
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val json = JSONObject(responseData)
                    val url = json.getString("url").toUri()
                    callback(url)
                } else {
                    callback(null)
                }
            }
        })
    }

    fun uploadData(data: Any, callback: (Boolean) -> Unit) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = gson.toJson(data).toRequestBody(mediaType)
        var uploadType = when (data) {
            is User -> "users"
            is Comment -> "comments"
            is DynamicPost -> "dynamicposts"
            is EasyPoint -> "easypoints"
            else -> throw IllegalArgumentException("unsupported data")
        }
        val request = Request.Builder().url("$baseUrl:5000/$uploadType").post(requestBody).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun checkForUpdate(callback: (UpdateInfo?) -> Unit) {
        val request = Request.Builder().url("$baseUrl/checkUpdate").build()
        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "onFailure: ${e.message}")
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    Log.e(TAG, "onResponse: ")
                    val responseData = response.body?.string()
                    val updateInfo = gson.fromJson(responseData, UpdateInfo::class.java)
                    callback(updateInfo)
                } else {
                    callback(null)
                }
            }
        })
    }
}