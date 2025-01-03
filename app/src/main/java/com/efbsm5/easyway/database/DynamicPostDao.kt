package com.efbsm5.easyway.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.efbsm5.easyway.data.DynamicPost

@Dao
interface DynamicPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dynamicPost: DynamicPost)

    @Query("SELECT * FROM dynamicposts WHERE id = :id")
    fun getDynamicPostById(id: Int): DynamicPost?

    @Query("SELECT * FROM dynamicposts")
    fun getAllDynamicPosts(): List<DynamicPost>

    @Query("DELETE FROM dynamicposts WHERE id = :id")
    fun deleteDynamicPostById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<DynamicPost>)

    @Query("DELETE FROM dynamicposts WHERE id IN (:ids)")
    fun deleteAll(ids: List<Int>)
}