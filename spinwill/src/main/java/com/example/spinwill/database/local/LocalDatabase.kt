package com.example.spinwill.database.local

import com.example.spinwill.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

interface SpinWillLocalDatabase<item> {
    suspend fun updateWillItems(newItemList: List<item>): Resource<Unit>
    suspend fun getAllWillItems(): Resource<List<item>>
}

class SpinWillLocalDbImpl<item> constructor(
    private val dao: SpinWillDbActions<item>
) : SpinWillLocalDatabase<item> {
    override suspend fun updateWillItems(newItemList: List<item>): Resource<Unit> =
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

    override suspend fun getAllWillItems(): Resource<List<item>> =
        withContext(Dispatchers.IO) {
            try {
                Resource.Success(dao.getAllItems())
            } catch (e: Exception) {
                Resource.Error(e.message)
            }
        }

}