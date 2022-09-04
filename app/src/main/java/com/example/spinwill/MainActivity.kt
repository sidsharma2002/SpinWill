package com.example.spinwill

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.work.*
import com.example.spinwill.cron.SpinWillWorker
import com.example.spinwill.database.local.SpinWillLocalDbImpl
import com.example.spinwill.di.SpinWillInjector
import com.example.spinwill.usecases.SpinWillBitmapLoadUseCaseImpl
import com.example.spinwill.utils.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var iv1: ImageView
    private lateinit var iv2: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDependencies()
       // setupWork()
        setupUi()
    }

    private fun setupDependencies() {
        SpinWillInjector.init(this, RemoteDatabaseImpl())
        SpinWillInjector.setLocalDatabase(
            SpinWillLocalDbImpl(SpinWillInjector.getSpinWillItemsDao())
        )
        SpinWillInjector.setBitmapLoadUseCase(SpinWillBitmapLoadUseCaseImpl())
    }

    private fun setupUi() {
        iv1 = findViewById(R.id.iv_1)
        iv2 = findViewById(R.id.iv_2)

        GlobalScope.launch {

            SpinWillInjector.getRepository().fetchAndUpdateWheelItem()
            val result = SpinWillInjector.getRepository().loadBitmapAndSave()

            if (result is Resource.Success && result.data != null) {
                val list = result.data!!
                Log.d("MainAct", "result size " + list.size)

                if (result.data!!.isNotEmpty()) {
                    Log.d("MainAct", "result " + list[0].toString())
                    runOnUiThread {
                        Log.d("spinwill","bitmap set 44 " + System.currentTimeMillis())
                        iv1.setImageBitmap(result.data!![0].rewardBitmap)
                    }
                }

                if (result.data!!.size >= 2) {
                    runOnUiThread {
                        Log.d("spinwill","bitmap set 51 " + System.currentTimeMillis())
                        iv2.setImageBitmap(result.data!![1].rewardBitmap)
                    }
                }
            }
        }

    }

    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    private fun setupWork() {
        val workRequest: PeriodicWorkRequest = PeriodicWorkRequest.Builder(
            SpinWillWorker::class.java, 2, TimeUnit.MINUTES
        ).setBackoffCriteria(
            BackoffPolicy.LINEAR,
            PeriodicWorkRequest.DEFAULT_BACKOFF_DELAY_MILLIS,
            TimeUnit.MILLISECONDS
        ).setConstraints(
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).addTag("task_update").build()

        Configuration.Builder().setMinimumLoggingLevel(Log.DEBUG).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "task_update",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )

    }
}