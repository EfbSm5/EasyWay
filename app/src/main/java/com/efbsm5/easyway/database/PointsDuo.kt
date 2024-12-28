package com.efbsm5.easyway.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.EasyPoints
import com.efbsm5.easyway.data.EasyPointsSimplify

@Dao
interface PointsDuo {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(point: EasyPoints)

    @Query("SELECT COUNT(*) FROM points")
    fun getCount(): Int

    @Query("select id,location,name from points order by id desc ")
    fun loadAllPoints(): List<EasyPointsSimplify>

    @Query("SELECT * FROM points WHERE id = :id")
    fun getPointById(id: Int): EasyPoints?

    @Query("UPDATE points SET `like` = `like` + 1 WHERE id = :id")
    fun incrementLikes(id: Int)

    @Query("UPDATE points SET dislike = dislike + 1 WHERE id = :id")
    fun incrementDislikes(id: Int)

    @Query("SELECT * FROM points WHERE pointId = :pointid")
    fun getHistory(pointid: Int): List<EasyPoints>

    @Query("UPDATE points SET comment = :comment WHERE id = :id")
    fun updateComment(id: Int, comment: Comment)

    @Query("SELECT comment FROM points WHERE id = :id")
    fun getCommentById(id: Int): Comment?
}