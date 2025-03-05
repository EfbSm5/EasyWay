package com.efbsm5.easyway.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.User

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

    @Query("SELECT COUNT(*) FROM dynamicposts")
    fun getCount(): Int

    @Query("SELECT * FROM dynamicposts WHERE userId = :userId")
    fun getAllDynamicPostsByUserId(userId: Int): List<DynamicPost>
}