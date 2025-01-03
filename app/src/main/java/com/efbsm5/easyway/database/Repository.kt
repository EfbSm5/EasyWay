package com.efbsm5.easyway.database

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.network.HttpClient

class Repository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext, AppDataBase::class.java, "app_database"
    ).build()
    private val commentDao = db.commentDao()
    private val userDao = db.userDao()
    private val dynamicPostDao = db.dynamicPostDao()
    private val pointsDao = db.pointsDao()
    private val httpClient = HttpClient("https://your.api.url")

    fun getComments(): MutableLiveData<List<Comment>?> {
        val data = MutableLiveData<List<Comment>?>()
        httpClient.getAllComments { comments ->
            if (comments != null) {
                data.postValue(comments)
                db.runInTransaction {
                    commentDao.insertAll(comments)
                }
            }
        }
        return data
    }

    fun syncData() {
        syncComments()
        syncUsers()
        syncDynamicPosts()
        syncEasyPoints()
    }

    private fun syncComments() {
        httpClient.getAllComments { networkComments ->
            if (networkComments != null) {
                val localComments = commentDao.getAllComments()
                val toInsert = networkComments.filter { it !in localComments }
                val toDelete = localComments.filter { it !in networkComments }.map { it.id }
                db.runInTransaction {
                    commentDao.deleteAll(toDelete)
                    commentDao.insertAll(toInsert)
                }
            }
        }
    }

    private fun syncUsers() {
        httpClient.getAllUsers { networkUsers ->
            if (networkUsers != null) {
                val localUsers = userDao.getAllUsers()
                val toInsert = networkUsers.filter { it !in localUsers }
                val toDelete = localUsers.filter { it !in networkUsers }.map { it.id }
                db.runInTransaction {
                    userDao.deleteAll(toDelete)
                    userDao.insertAll(toInsert)
                }
            }
        }
    }

    private fun syncDynamicPosts() {
        httpClient.getAllDynamicPosts { networkPosts ->
            if (networkPosts != null) {
                val localPosts = dynamicPostDao.getAllDynamicPosts()
                val toInsert = networkPosts.filter { it !in localPosts }
                val toDelete = localPosts.filter { it !in networkPosts }.map { it.id }
                db.runInTransaction {
                    dynamicPostDao.deleteAll(toDelete)
                    dynamicPostDao.insertAll(toInsert)
                }
            }
        }
    }

    private fun syncEasyPoints() {
        httpClient.getAllEasyPoints { networkPoints ->
            if (networkPoints != null) {
                val localPoints = pointsDao.loadAllPoints()
                val toInsert =
                    networkPoints.filter { networkPoint -> localPoints.none { it.id  == networkPoint.pointId } }
                val toDelete =
                    localPoints.filter { localPoint -> networkPoints.none { it.pointId == localPoint.id } }
                        .map { it.id }
                db.runInTransaction {
                    pointsDao.deleteAll(toDelete)
                    pointsDao.insertAll(toInsert)
                }
            }
        }
    }
}