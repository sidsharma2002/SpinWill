package com.example.spinwill.database.remote

import com.example.spinwill.models.SpinWillItem
import com.example.spinwill.utils.Resource

interface SpinWillRemoteDatabase {
    suspend fun fetch(): Resource<List<SpinWillItem>>
}