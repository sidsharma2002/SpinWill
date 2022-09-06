package com.example.spinwill

import android.content.Context
import androidx.room.*
import com.example.spinwill.database.local.SpinWillDbActions

@Database(entities = [SpinWillItem::class], version = 1, exportSchema = false)
abstract class AppWillRoomDatabase : RoomDatabase() {

    abstract fun getSpinWillDao(): SpinWillDaoImpl

    companion object {
        @Volatile
        private var instance: AppWillRoomDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): AppWillRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }
        }

        // Create the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): AppWillRoomDatabase {
            return Room.databaseBuilder(
                context,
                AppWillRoomDatabase::class.java,
                "SPINWILL_DB_123"
            ).fallbackToDestructiveMigration().build()
        }
    }
}


@Dao
interface SpinWillDaoImpl {
    @Query("SELECT * FROM spinwill_item")
    suspend fun getAllItems(): List<SpinWillItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(willItems: List<SpinWillItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(willItem: SpinWillItem)

    @Delete
    suspend fun delete(willItem: SpinWillItem)

    @Query("DELETE FROM spinwill_item")
    suspend fun deleteAll()
}

class SpinWillDaoConc : SpinWillDbActions<SpinWillItem> {

    private lateinit var dao: SpinWillDaoImpl

    fun setDao(dao: SpinWillDaoImpl) {
        this.dao = dao
    }

    override suspend fun getAllItems(): List<SpinWillItem> {
        return dao.getAllItems()
    }

    override suspend fun insert(willItem: SpinWillItem) {
        return dao.insert(willItem)
    }

    override suspend fun insertAll(willItems: List<SpinWillItem>) {
        return dao.insertAll(willItems)
    }

    override suspend fun delete(willItem: SpinWillItem) {
        return dao.delete(willItem)
    }

    override suspend fun deleteAll() {
        return dao.deleteAll()
    }
}