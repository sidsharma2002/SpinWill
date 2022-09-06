package com.example.spinwill.adapter

import android.graphics.Bitmap

interface WillItemAdapter<item> {
    fun getRewardImageUrl(item: item): String
    fun setRewardBitmap(item: item, bitmap: Bitmap)
}