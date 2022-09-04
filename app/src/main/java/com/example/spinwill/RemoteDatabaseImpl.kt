package com.example.spinwill

import android.util.Log
import com.example.spinwill.database.remote.SpinWillRemoteDatabase
import com.example.spinwill.models.SpinWillItem
import com.example.spinwill.utils.Resource
import kotlinx.coroutines.delay

class RemoteDatabaseImpl : SpinWillRemoteDatabase {
    override suspend fun fetch(): Resource<List<SpinWillItem>> {
        Log.d("App", "23")
        val list = mutableListOf<SpinWillItem>()

        list.add(
            SpinWillItem(
                rewardId = 1L,
                rewardAmount = 100L,
                rewardImage = "https://images.pexels.com/photos/2014422/pexels-photo-2014422.jpeg",
                rewardText = "100 COINS"
            )
        )

        delay(2000)

        list.add(
            SpinWillItem(
                rewardId = 1L,
                rewardAmount = 100L,
                rewardImage = "https://images.pexels.com/photos/2014422/pexels-photo-2014422.jpeg",
                rewardText = "100 COINS"
            )
        )

        Log.d("App", "19")

        return Resource.Success(list)
    }
}