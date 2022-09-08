package com.example.spinwill.ui.adapters

import android.graphics.Bitmap

interface WillItemUiAdapter<T> {
    fun getRewardText(item: T): String
    fun getOverlayText(item: T): String
    fun getRewardBitmap(item: T): Bitmap?
}