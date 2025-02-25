package com.efbsm5.easyway.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.efbsm5.easyway.data.models.Photo

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo)

    @Query("SELECT * FROM photos WHERE id = :id")
    fun getPhotoById(id: Int): List<Photo>

    @Query("SELECT * FROM photos")
    fun getAllPhotos(): List<Photo>

    @Query("DELETE FROM photos WHERE id = :id")
    fun deletePhotosById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(photos: List<Photo>)

    @Query("DELETE FROM photos  WHERE id IN (:ids)")
    fun deleteAll(ids: List<Int>)

    @Query("SELECT COUNT(*) FROM photos")
    fun getCount(): Int

    @Query("SELECT MAX(photoId) FROM photos")
    fun getMaxCommentId(): Int
}