package com.example.spinwill.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.spinwill.config.SpinWillConfig
import com.example.spinwill.models.SpinWillItem

@Database(entities = [SpinWillItem::class], version = 2, exportSchema = true)
abstract class SpinWillRoomDatabase : RoomDatabase() {

    abstract fun getSpinWillDao(): SpinWillDao

    companion object {
        @Volatile
        private var instance: SpinWillRoomDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): SpinWillRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        // Create the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): SpinWillRoomDatabase {
            return Room.databaseBuilder(
                context,
                SpinWillRoomDatabase::class.java,
                SpinWillConfig.SPINWILL_DB
            ).fallbackToDestructiveMigration().build()
        }
    }
}