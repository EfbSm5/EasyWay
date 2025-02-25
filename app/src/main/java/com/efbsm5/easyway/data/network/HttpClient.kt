package com.efbsm5.easyway.data.network

import android.content.Context
import android.net.Uri
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.database.UriTypeAdapter
import com.efbsm5.easyway.data.models.User
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class HttpClient(url: String) {
    private val client = OkHttpClient()
    private val baseUrl = url
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

    fun uploadImage(context: Context, uri: Uri, callback: (String?) -> Unit) {
        val file = uriToFile(context, uri)
        val client = OkHttpClient()
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
                    val path = json.getString("path")
                    callback(path)
                } else {
                    callback(null)
                }
            }
        })
    }
}