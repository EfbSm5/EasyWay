package com.efbsm5.easyway.network

import android.content.Context
import android.net.Uri
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.data.EasyPointSimplify
import com.efbsm5.easyway.data.User
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class HttpClient(url: String) {
    private val client = OkHttpClient()
    private val baseUrl = url
    private val gson = Gson()

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

    fun getCommentsById(id: Int, callback: (List<Comment>?) -> Unit) {
        val request = Request.Builder().url("$baseUrl/comments/$id").build()

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

    fun addComment(comment: Comment, callback: (Boolean) -> Unit) {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(comment)
        val body = json.toRequestBody(JSON)
        val request = Request.Builder().url("$baseUrl/comments").post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun updateComment(id: Int, comment: Comment, callback: (Boolean) -> Unit) {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(comment)
        val body = json.toRequestBody(JSON)
        val request = Request.Builder().url("$baseUrl/comments/$id").put(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun deleteComment(id: Int, callback: (Boolean) -> Unit) {
        val request = Request.Builder().url("$baseUrl/comments/$id").delete().build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }


    fun getUser(id: Int, callback: (User?) -> Unit) {
        val request = Request.Builder().url("$baseUrl/users/$id").build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val user = gson.fromJson(responseData, User::class.java)
                    callback(user)
                } else {
                    callback(null)
                }
            }
        })
    }

    fun addUser(user: User, callback: (Boolean) -> Unit) {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(user)
        val body = json.toRequestBody(JSON)
        val request = Request.Builder().url("$baseUrl/users").post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun updateUser(id: Int, user: User, callback: (Boolean) -> Unit) {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(user)
        val body = json.toRequestBody(JSON)
        val request = Request.Builder().url("$baseUrl/users/$id").put(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun deleteUser(id: Int, callback: (Boolean) -> Unit) {
        val request = Request.Builder().url("$baseUrl/users/$id").delete().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun getEasyPointSimplify(callback: (List<EasyPointSimplify>?) -> Unit) {
        val request = Request.Builder().url("$baseUrl/easypoints/simplify").build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val points =
                        gson.fromJson(responseData, Array<EasyPointSimplify>::class.java).toList()
                    callback(points)
                } else {
                    callback(null)
                }
            }
        })
    }

    fun addEasyPoint(point: EasyPoint, callback: (Boolean) -> Unit) {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(point)
        val body = json.toRequestBody(JSON)
        val request = Request.Builder().url("$baseUrl/easypoints").post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun updateEasyPoint(id: Int, point: EasyPoint, callback: (Boolean) -> Unit) {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(point)
        val body = json.toRequestBody(JSON)
        val request = Request.Builder().url("$baseUrl/easypoints/$id").put(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun deleteEasyPoint(id: Int, callback: (Boolean) -> Unit) {
        val request = Request.Builder().url("$baseUrl/easypoints/$id").delete().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun getEasyPoint(id: Int, callback: (EasyPoint?) -> Unit) {
        val request = Request.Builder().url("$baseUrl/easypoints/$id").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val easyPoint = gson.fromJson(responseData, EasyPoint::class.java)
                    callback(easyPoint)
                } else {
                    callback(null)
                }
            }
        })
    }


    fun addDynamicPost(post: DynamicPost, callback: (Boolean) -> Unit) {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(post)
        val body = json.toRequestBody(JSON)
        val request = Request.Builder().url("$baseUrl/dynamicposts").post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun updateDynamicPost(id: Int, post: DynamicPost, callback: (Boolean) -> Unit) {
        val JSON = "application/json; charset=utf-8".toMediaType()
        val json = gson.toJson(post)
        val body = json.toRequestBody(JSON)
        val request = Request.Builder().url("$baseUrl/dynamicposts/$id").put(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun deleteDynamicPost(id: Int, callback: (Boolean) -> Unit) {
        val request = Request.Builder().url("$baseUrl/dynamicposts/$id").delete().build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }

    fun getDynamicPost(id: Int, callback: (DynamicPost?) -> Unit) {
        val request = Request.Builder().url("$baseUrl/dynamicposts/$id").build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(null)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    val post = gson.fromJson(responseData, DynamicPost::class.java)
                    callback(post)
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

    fun uploadImage(context: Context, uri: Uri, url: String, callback: (Boolean) -> Unit) {
        val file = uriToFile(context, uri)
        val client = OkHttpClient()
        val mediaType = "image/jpeg".toMediaType()
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("photo", file.name, file.asRequestBody(mediaType)).build()

        val request = Request.Builder().url(url).post(requestBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                callback(false)
            }

            override fun onResponse(call: Call, response: Response) {
                callback(response.isSuccessful)
            }
        })
    }
}