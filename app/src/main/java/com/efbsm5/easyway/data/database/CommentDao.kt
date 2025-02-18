package com.efbsm5.easyway.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.efbsm5.easyway.data.models.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comment: Comment)

    @Query("SELECT * FROM comments WHERE comment_id = :id")
    fun getCommentByCommentId(id: Int): List<Comment>

    @Query("SELECT * FROM comments")
    fun getAllComments(): List<Comment>

    @Query("DELETE FROM comments WHERE `index` = :index")
    fun deleteCommentByIndex(index: Int)

    @Query("UPDATE comments SET `like` = `like` + 1 WHERE `index` = :id")
    fun increaseLikes(id: Int)

    @Query("UPDATE comments SET dislike = dislike + 1 WHERE `index` = :id")
    fun increaseDislikes(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(posts: List<Comment>)

    @Query("DELETE FROM comments WHERE comment_id IN (:ids)")
    fun deleteAll(ids: List<Int>)

    @Query("SELECT COUNT(*) FROM comments")
    fun getCount(): Int

//    @Query("SELECT COUNT(*) FROM comments WHERE comment_id = :commentId ")
//    fun getCountById(commentId: Int): Int

    @Query("SELECT MAX(comment_id) FROM comments")
    fun getMaxCommentId(): Int
}