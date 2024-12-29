package com.efbsm5.easyway.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amap.api.maps.model.Marker
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.EasyPoints
import com.efbsm5.easyway.data.EasyPointsSimplify
import com.efbsm5.easyway.data.MarkerData

@Dao
interface PointsDuo {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(point: EasyPoints)

    @Query("SELECT COUNT(*) FROM points")
    fun getCount(): Int

    @Query("select id,pointId,marker,name from points order by id desc ")
    fun loadAllPoints(): List<EasyPointsSimplify>

    @Query("SELECT * FROM points WHERE id = :id")
    fun getPointById(id: Int): EasyPoints?

    @Query("UPDATE points SET `like` = `like` + 1 WHERE id = :id")
    fun incrementLikes(id: Int)

    @Query("UPDATE points SET dislike = dislike + 1 WHERE id = :id")
    fun incrementDislikes(id: Int)

    @Query("SELECT * FROM points WHERE pointId = :pointId")
    fun getHistory(pointId: Int): List<EasyPoints>

    @Query("UPDATE points SET comment = :comment WHERE id = :id")
    fun updateComment(id: Int, comment: String)

    @Query("SELECT comment FROM points WHERE id = :id")
    fun getCommentById(id: Int): String?

    @Query("SELECT * FROM points WHERE marker=:marker")
    fun markerToPoints(marker: MarkerData): EasyPoints
}