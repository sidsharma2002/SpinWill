package com.example.spinwill

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "spinwill_item")
data class SpinWillItem(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var rewardId: Long = -1,
    var rewardAmount: Long = -1,
    var rewardText: String = "",
    var rewardImage: String = "",
    @Ignore
    var rewardBitmap: Bitmap? = null
)
