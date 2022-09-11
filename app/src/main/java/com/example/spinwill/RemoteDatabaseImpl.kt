package com.example.spinwill

import android.util.Log
import com.example.spinwill.database.remote.SpinWillRemoteDatabase
import com.example.spinwill.utils.Resource
import kotlinx.coroutines.delay

class RemoteDatabaseImpl : SpinWillRemoteDatabase<SpinWillItem> {
    override suspend fun fetch(): Resource<List<SpinWillItem>> {
        Log.d("App", "23")
        val list = mutableListOf<SpinWillItem>()

        /*
            when number of items is odd it creates repetition in color of arcs
         */
        for (i in 0..5) {
            list.add(
                SpinWillItem(
                    rewardId = 1L,
                    rewardAmount = i * 100L,
                    rewardImage = "https://images.pexels.com/photos/2014422/pexels-photo-2014422.jpeg",
                    rewardText = "${i}00"
                )
            )
        }
        Log.d("App", "19")
        return Resource.Success(list)
    }
}