package com.efbsm5.easyway.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.data.EasyPointSimplify

@Dao
interface PointsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(point: EasyPoint)

    @Query("SELECT COUNT(*) FROM points")
    fun getCount(): Int

    @Query("select pointId,name,lat,lng from points order by pointId desc ")
    fun loadAllPoints(): List<EasyPointSimplify>

    @Query("SELECT * FROM points WHERE pointId = :id")
    fun getPointById(id: Int): EasyPoint?

    @Query("UPDATE points SET `like` = `like` + 1 WHERE pointId = :id")
    fun incrementLikes(id: Int)

    @Query("UPDATE points SET dislike = dislike + 1 WHERE pointId = :id")
    fun incrementDislikes(id: Int)

    @Query("SELECT * FROM points WHERE pointId = :pointId")
    fun getHistory(pointId: Int): List<EasyPoint>

    @Query("SELECT * FROM points WHERE lat = :lat AND lng = :lng")
    fun getPointByLatLng(lat: Double, lng: Double): EasyPoint?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<EasyPoint>)

    @Query("DELETE FROM points WHERE pointId IN (:ids)")
    fun deleteAll(ids: List<Int>)
}