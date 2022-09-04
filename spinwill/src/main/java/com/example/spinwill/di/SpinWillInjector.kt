package com.example.spinwill.di

import android.annotation.SuppressLint
import android.content.Context
import com.example.spinwill.database.local.SpinWillDao
import com.example.spinwill.database.local.SpinWillRoomDatabase
import com.example.spinwill.database.local.SpinWillLocalDatabase
import com.example.spinwill.database.remote.SpinWillRemoteDatabase
import com.example.spinwill.repository.SpinWillRepository
import com.example.spinwill.usecases.SpinWillBitmapLoadUseCase

@SuppressLint("StaticFieldLeak")
object SpinWillInjector {

    private lateinit var context: Context

    /**
     * provide application context to initialize database.
     */
    fun provideContext(context: Context) {
        this.context = context
    }

    fun getContext(): Context {
        checkCtxInitialization()
        return context
    }

    fun init(context: Context, remoteDatabase: SpinWillRemoteDatabase) {
        provideContext(context)
        setRemoteDatabase(remoteDatabase)
    }

    fun getSpinWillItemsDao(): SpinWillDao {
        checkCtxInitialization()
        return SpinWillRoomDatabase.getInstance(context).getSpinWillDao()
    }

    private lateinit var localDatabase: SpinWillLocalDatabase

    fun setLocalDatabase(localDatabase: SpinWillLocalDatabase) {
        this.localDatabase = localDatabase
    }

    fun getLocalDatabase(): SpinWillLocalDatabase {
        checkCtxInitialization()
        return localDatabase
    }

    private lateinit var remoteDatabase: SpinWillRemoteDatabase

    fun getRemoteDatabase(): SpinWillRemoteDatabase {
        if (::remoteDatabase.isInitialized)
            return remoteDatabase

        throw RuntimeException("remote database not initialized!")
    }

    fun setRemoteDatabase(remoteDatabase: SpinWillRemoteDatabase) {
        this.remoteDatabase = remoteDatabase
    }

    private lateinit var bitmapLoadUseCase: SpinWillBitmapLoadUseCase

    fun setBitmapLoadUseCase(useCase: SpinWillBitmapLoadUseCase) {
        bitmapLoadUseCase = useCase
    }

    fun getBitmapLoadUseCase(): SpinWillBitmapLoadUseCase = bitmapLoadUseCase

    private lateinit var repository: SpinWillRepository

    fun getRepository(): SpinWillRepository {
        if (::repository.isInitialized)
            return repository

        repository =
            SpinWillRepository(getLocalDatabase(), getRemoteDatabase(), getBitmapLoadUseCase())
        return repository
    }

    private fun checkCtxInitialization() {
        if (::context.isInitialized.not()) {
            throw RuntimeException("context has not been provided")
        }
    }
}