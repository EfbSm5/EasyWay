package com.efbsm5.easyway.data.network

import android.content.Context
import androidx.room.Room
import com.efbsm5.easyway.data.database.AppDataBase

class Repository(context: Context) {
    private val db = Room.databaseBuilder(
        context.applicationContext, AppDataBase::class.java, "app_database"
    ).build()
    private val commentDao = db.commentDao()
    private val userDao = db.userDao()
    private val dynamicPostDao = db.dynamicPostDao()
    private val pointsDao = db.pointsDao()
    private val httpClient = HttpClient("https://f35b-2001-df0-a640-1-00-4.ngrok-free.app")

    fun syncData() {
        syncUsers()
        syncComments()
        syncDynamicPosts()
        syncEasyPoints()
    }

    private fun syncComments() {
        httpClient.getAllComments { networkComments ->
            if (networkComments != null) {
                val localComments = commentDao.getAllComments()
                val toInsert = networkComments.filter { it !in localComments }
                val toDelete = localComments.filter { it !in networkComments }.map { it.comment_id }
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
                    networkPoints.filter { networkPoint -> localPoints.none { it.pointId  == networkPoint.pointId } }
                val toDelete =
                    localPoints.filter { localPoint -> networkPoints.none { it.pointId == localPoint.pointId } }
                        .map { it.pointId }
                db.runInTransaction {
                    pointsDao.deleteAll(toDelete)
                    pointsDao.insertAll(toInsert)
                }
            }
        }
    }
}