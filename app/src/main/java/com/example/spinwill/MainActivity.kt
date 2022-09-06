package com.example.spinwill

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.work.*
import com.example.spinwill.adapter.WillItemAdapter
import com.example.spinwill.cron.SpinWillWorker
import com.example.spinwill.cron.SpinWillWorkerFactory
import com.example.spinwill.database.local.SpinWillLocalDbImpl
import com.example.spinwill.di.SpinWillInjector
import com.example.spinwill.usecases.SpinWillBitmapLoadUseCaseImpl
import com.example.spinwill.utils.Resource
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var iv1: ImageView
    private lateinit var iv2: ImageView
    private val scope = CoroutineScope(Job())
    private val injector by lazy {
        SpinWillInjector<SpinWillItem>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AppWillRoomDatabase.getInstance(this.applicationContext)
        setupDependencies()
        // setupWork()
        setupUi()
    }

    private fun setupDependencies() {
        // set context, remoteDb, localDb, dao, bitmap load use-case.
        injector.init(this, RemoteDatabaseImpl())

        val daoConc = SpinWillDaoConc()
        daoConc.setDao(AppWillRoomDatabase.getInstance(this.applicationContext).getSpinWillDao())
        injector.setLocalDatabase(
            SpinWillLocalDbImpl(daoConc)
        )

        injector.setBitmapLoadUseCase(
            SpinWillBitmapLoadUseCaseImpl(
                this.applicationContext,
                object : WillItemAdapter<SpinWillItem> {
                    override fun getRewardImageUrl(item: SpinWillItem): String {
                        return item.rewardImage
                    }

                    override fun setRewardBitmap(item: SpinWillItem, bitmap: Bitmap) {
                        item.rewardBitmap = bitmap
                    }
                })
        )
    }

    private fun setupUi() {
        iv1 = findViewById(R.id.iv_1)
        iv2 = findViewById(R.id.iv_2)

        scope.launch {

            injector.getRepository().fetchAndUpdateWheelItem()
            val result = injector.getRepository().loadBitmapAndSave()

            if (result is Resource.Success && result.data != null) {
                val list = result.data!!
                Log.d("MainAct", "result size " + list.size)

                if (result.data!!.isNotEmpty()) {
                    Log.d("MainAct", "result " + list[0].toString())
                    runOnUiThread {
                        Log.d("spinwill", "bitmap set 44 " + System.currentTimeMillis())
                        iv1.setImageBitmap(result.data!![0].rewardBitmap)
                    }
                }

                if (result.data!!.size >= 2) {
                    runOnUiThread {
                        Log.d("spinwill", "bitmap set 51 " + System.currentTimeMillis())
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

        Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(SpinWillWorkerFactory(injector.getRepository()))
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "task_update",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.coroutineContext.cancelChildren()
    }
}