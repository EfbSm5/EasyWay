package com.efbsm5.easyway.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.efbsm5.easyway.data.EasyPoints

@Dao
interface PointsDuo {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(point: EasyPoints)

    @Query("SELECT COUNT(*) FROM points")
    fun getCount(): Int

    @Query("select * from points order by id desc ")
    fun loadAllPoints(): List<EasyPoints>

    @Query("UPDATE points SET `like` = `like` + 1 WHERE id = :id")
    fun incrementLikes(id: Int)

    @Query("UPDATE points SET dislike = dislike + 1 WHERE id = :id")
    fun incrementDislikes(id: Int)

}