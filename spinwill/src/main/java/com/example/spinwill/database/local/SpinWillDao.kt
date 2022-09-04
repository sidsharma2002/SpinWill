package com.example.spinwill.database.local

import androidx.room.*
import com.example.spinwill.models.SpinWillItem

@Dao
interface SpinWillDao {
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