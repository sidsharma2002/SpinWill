package com.example.spinwill

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.work.*
import com.example.spinwill.config.SpinWillConfig
import com.example.spinwill.cron.SpinWillWorker
import com.example.spinwill.database.local.SpinWillLocalDbImpl
import com.example.spinwill.di.SpinWillInjector
import com.example.spinwill.usecases.SpinWillBitmapLoadUseCaseImpl
import java.util.concurrent.TimeUnit

class App : Application() {

    @SuppressLint("InvalidPeriodicWorkRequestInterval")
    override fun onCreate() {
        super.onCreate()
        Log.d("App", "52")
    }
}