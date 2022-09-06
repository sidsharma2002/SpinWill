package com.example.spinwill.cron

import android.content.Context
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.spinwill.di.SpinWillInjector
import com.example.spinwill.repository.SpinWillRepository
import com.example.spinwill.utils.Resource

class SpinWillWorker<item> constructor(
    private val context: Context,
    workerParameters: WorkerParameters,
    private val repository: SpinWillRepository<item>
) : CoroutineWorker(context, workerParameters) {


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


class SpinWillWorkerFactory<item> constructor(private val repository: SpinWillRepository<item>) :
    WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        // This only handles a single Worker, please don’t do this!!
        // See below for a better way using DelegatingWorkerFactory
        return SpinWillWorker(appContext, workerParameters, repository)
    }
}
