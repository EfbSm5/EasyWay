package com.efbsm5.easyway.data.network

import androidx.room.Room
import com.efbsm5.easyway.Myapplication
import com.efbsm5.easyway.data.database.AppDataBase

object IntentRepository {
    private val db = Room.databaseBuilder(
        Myapplication.getContext(), AppDataBase::class.java, "app_database"
    ).build()
    private val commentDao = db.commentDao()
    private val userDao = db.userDao()
    private val dynamicPostDao = db.dynamicPostDao()
    private val pointsDao = db.pointsDao()

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
                localComments.filter { it !in networkComments }.map { it.commentId }
                db.runInTransaction {
//                    commentDao.deleteAll(toDelete)
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
                localUsers.filter { it !in networkUsers }.map { it.id }
                db.runInTransaction {
//                    userDao.deleteAll(toDelete)
                    userDao.insertAll(toInsert)
                }
            }
        }
    }

    private fun syncDynamicPosts() {
        httpClient.getAllDynamicPosts { networkPosts ->
            if (networkPosts != null) {
                val localPosts = dynamicPostDao.getAllDynamicPostsByOnce()
                val toInsert = networkPosts.filter { it !in localPosts }
                localPosts.filter { it !in networkPosts }.map { it.id }
                db.runInTransaction {
//                    dynamicPostDao.deleteAll(toDelete)
                    dynamicPostDao.insertAll(toInsert)
                }
            }
        }
    }

    private fun syncEasyPoints() {
        httpClient.getAllEasyPoints { networkPoints ->
            if (networkPoints != null) {
                val localPoints = pointsDao.loadAllPointsByOnce()
                val toInsert =
                    networkPoints.filter { networkPoint -> localPoints.none { it.pointId == networkPoint.pointId } }
                localPoints.filter { localPoint -> networkPoints.none { it.pointId == localPoint.pointId } }
                    .map { it.pointId }
                db.runInTransaction {
//                    pointsDao.deleteAll(toDelete)
                    pointsDao.insertAll(toInsert)
                }
            }
        }
    }


}