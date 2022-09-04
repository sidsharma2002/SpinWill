package com.example.spinwill.cron

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.spinwill.di.SpinWillInjector
import com.example.spinwill.utils.Resource

class SpinWillWorker constructor(
    private val context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val repository by lazy { SpinWillInjector.getRepository() }

    override suspend fun doWork(): Result {
        Log.d("spinwill", "doWork")
        return updateItemsInBackground()
    }

    private suspend fun updateItemsInBackground(): Result {
        val result1 = repository.fetchAndUpdateWheelItem()

        Log.d("spinwill", "result : ${result1.data}")

        if (result1 is Resource.Error) {
            return Result.retry()
        }

        val result = repository.loadBitmapAndSave()

        return if (result is Resource.Success)
            Result.success()
        else
            Result.retry()
    }
}