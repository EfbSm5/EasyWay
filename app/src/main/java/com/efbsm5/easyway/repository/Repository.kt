package com.efbsm5.easyway.repository


import android.content.Context
import com.efbsm5.easyway.data.EasyPointSimplify
import com.efbsm5.easyway.database.AppDataBase

class PointsRepository(private val context: Context) {
    private val pointsDao = AppDataBase.getDatabase(context).pointsDao()

    suspend fun getAllPoints(): List<EasyPointSimplify> {
        return pointsDao.loadAllPoints()
    }
}