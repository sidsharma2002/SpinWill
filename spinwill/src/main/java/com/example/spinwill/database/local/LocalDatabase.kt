package com.example.spinwill.database.local

import com.example.spinwill.models.SpinWillItem
import com.example.spinwill.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

interface SpinWillLocalDatabase {
    suspend fun updateWillItems(newItemList: List<SpinWillItem>): Resource<Unit>
    suspend fun getAllWillItems(): Resource<List<SpinWillItem>>
}

class SpinWillLocalDbImpl constructor(
    private val dao: SpinWillDao
) : SpinWillLocalDatabase {
    override suspend fun updateWillItems(newItemList: List<SpinWillItem>): Resource<Unit> =
        withContext(Dispatchers.IO) {
            withContext(NonCancellable + Dispatchers.IO) {
                try {
                    dao.deleteAll()
                    dao.insertAll(newItemList)
                    return@withContext Resource.Success(Unit)
                } catch (e: Exception) {
                    e.printStackTrace()
                    return@withContext Resource.Error(e.message)
                }
            }
        }

    override suspend fun getAllWillItems(): Resource<List<SpinWillItem>> =
        withContext(Dispatchers.IO) {
            try {
                Resource.Success(dao.getAllItems())
            } catch (e: Exception) {
                Resource.Error(e.message)
            }
        }

}