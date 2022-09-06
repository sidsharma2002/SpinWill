package com.example.spinwill.database.remote

import com.example.spinwill.utils.Resource

interface SpinWillRemoteDatabase <item> {
    suspend fun fetch(): Resource<List<item>>
}