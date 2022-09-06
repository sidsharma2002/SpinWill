package com.example.spinwill.di

import android.annotation.SuppressLint
import android.content.Context
import com.example.spinwill.database.local.SpinWillDbActions
import com.example.spinwill.database.local.SpinWillLocalDatabase
import com.example.spinwill.database.remote.SpinWillRemoteDatabase
import com.example.spinwill.repository.SpinWillRepository
import com.example.spinwill.usecases.SpinWillBitmapLoadUseCase

@SuppressLint("StaticFieldLeak")
class SpinWillInjector<spinWillItem> {

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

    fun init(context: Context, remoteDatabase: SpinWillRemoteDatabase<spinWillItem>) {
        provideContext(context)
        setRemoteDatabase(remoteDatabase)
    }

    private lateinit var spinWillDbActions: SpinWillDbActions<spinWillItem>

    fun getSpinWillItemsDbActions(): SpinWillDbActions<spinWillItem> {
        checkCtxInitialization()
        return spinWillDbActions
    }

    private lateinit var localDatabase: SpinWillLocalDatabase<spinWillItem>

    fun setLocalDatabase(localDatabase: SpinWillLocalDatabase<spinWillItem>) {
        this.localDatabase = localDatabase
    }

    fun getLocalDatabase(): SpinWillLocalDatabase<spinWillItem> {
        checkCtxInitialization()
        return localDatabase
    }

    private lateinit var remoteDatabase: SpinWillRemoteDatabase<spinWillItem>

    fun getRemoteDatabase(): SpinWillRemoteDatabase<spinWillItem> {
        if (::remoteDatabase.isInitialized)
            return remoteDatabase

        throw RuntimeException("remote database not initialized!")
    }

    fun setRemoteDatabase(remoteDatabase: SpinWillRemoteDatabase<spinWillItem>) {
        this.remoteDatabase = remoteDatabase
    }

    private lateinit var bitmapLoadUseCase: SpinWillBitmapLoadUseCase<spinWillItem>

    fun setBitmapLoadUseCase(useCase: SpinWillBitmapLoadUseCase<spinWillItem>) {
        bitmapLoadUseCase = useCase
    }

    fun getBitmapLoadUseCase(): SpinWillBitmapLoadUseCase<spinWillItem> = bitmapLoadUseCase

    private lateinit var repository: SpinWillRepository<spinWillItem>

    fun getRepository(): SpinWillRepository<spinWillItem> {
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