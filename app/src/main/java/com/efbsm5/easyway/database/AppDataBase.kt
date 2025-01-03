package com.efbsm5.easyway.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.efbsm5.easyway.data.*

@Database(
    version = 1,
    entities = [EasyPoint::class, User::class, Comment::class, DynamicPost::class],
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun pointsDao(): PointsDao
    abstract fun commentDao(): CommentDao
    abstract fun dynamicPostDao(): DynamicPostDao
    abstract fun userDao(): UserDao

    companion object {
        private var instance: AppDataBase? = null

        @Synchronized
        fun getDatabase(context: Context): AppDataBase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext, AppDataBase::class.java, "app_database"
            ).build().apply { instance = this }
        }
    }
}