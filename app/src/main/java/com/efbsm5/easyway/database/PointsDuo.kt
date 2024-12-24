package com.efbsm5.easyway.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PointsDuo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(Easy)

    @Query("SELECT COUNT(*) FROM profile")
    fun getCount(): Int

    @Query("select * from profile order by id desc limit 1;")
    fun loadUser(): UserProfile

    @Query("select * from profile order by id desc ")
    fun loadAllUsers(): List<UserProfile>

}