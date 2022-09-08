package com.example.spinwill.models

import android.graphics.Paint

data class WillPaintProperties(
    val archPaint: Paint = Paint(),
    val textPaint: Paint = Paint(),
    val overlayTextPaint: Paint = Paint(),
    val separationArchPaint: Paint = Paint(),
    val bitmapPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG)
)