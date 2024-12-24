package com.efbsm5.easyway.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.efbsm5.easyway.data.EasyPoints

@Database(version = 1, entities = [EasyPoints::class], exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun userDao(): PointsDuo

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