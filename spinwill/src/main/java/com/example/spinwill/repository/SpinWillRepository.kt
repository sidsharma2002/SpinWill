package com.example.spinwill.repository

import com.example.spinwill.database.local.SpinWillLocalDatabase
import com.example.spinwill.database.remote.SpinWillRemoteDatabase
import com.example.spinwill.usecases.SpinWillBitmapLoadUseCase
import com.example.spinwill.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext

class SpinWillRepository<item> constructor(
    private val localDatabase: SpinWillLocalDatabase<item>,
    private val remoteDatabase: SpinWillRemoteDatabase<item>,
    private val bitmapLoadUseCase: SpinWillBitmapLoadUseCase<item>
) {
    suspend fun fetchAndUpdateWheelItem(): Resource<Unit> = withContext(Dispatchers.IO) {
        withContext(NonCancellable + Dispatchers.IO) {
            val resultFromRemote: Resource<List<item>> = remoteDatabase.fetch()

            // if error thrown from remote, then don't go for updating the local db.
            if (resultFromRemote is Resource.Error || resultFromRemote.data == null) {
                return@withContext Resource.Error(resultFromRemote.message)
            }

            return@withContext localDatabase.updateWillItems(resultFromRemote.data)
        }
    }

    suspend fun loadBitmapAndSave(): Resource<List<item>> = withContext(Dispatchers.IO) {
        val result = localDatabase.getAllWillItems()

        if (result !is Resource.Success || result.data == null)
            return@withContext Resource.Error(result.message)

        return@withContext bitmapLoadUseCase.setBitmapFromUrl(result.data)
    }
}