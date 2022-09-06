package com.example.spinwill.database.local


interface SpinWillDbActions<T> {
    suspend fun getAllItems(): List<T>
    suspend fun insert(willItem: T)
    suspend fun insertAll(willItems: List<T>)
    suspend fun delete(willItem: T)
    suspend fun deleteAll()
}