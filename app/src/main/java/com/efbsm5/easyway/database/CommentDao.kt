package com.efbsm5.easyway.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.efbsm5.easyway.data.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: Comment)

    @Query("SELECT * FROM comments WHERE id = :id")
    fun getCommentByCommentId(id: Int): List<Comment>

    @Query("SELECT * FROM comments")
    fun getAllComments(): List<Comment>

    @Query("DELETE FROM comments WHERE `index` = :index")
    fun deleteCommentById(index: Int)

    @Query("UPDATE comments SET `like` = `like` + 1 WHERE `index` = :id")
    fun incrementLikes(id: Int)

    @Query("UPDATE comments SET dislike = dislike + 1 WHERE `index` = :id")
    fun incrementDislikes(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Comment>)

    @Query("DELETE FROM comments WHERE id IN (:ids)")
    fun deleteAll(ids: List<Int>)

    @Query("SELECT COUNT(*) FROM comments")
    fun getCount(): Int
}