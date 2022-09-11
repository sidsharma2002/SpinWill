package com.example.spinwill.models

import android.graphics.Paint
import com.example.spinwill.adapter.WillPaintAdapter

data class WillPaintProperties(
    val archPaint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL_AND_STROKE
    },
    val textPaint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        letterSpacing = 0.1f
    },
    val overlayTextPaint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        letterSpacing = 0.1f
    },
    val separationArchPaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
        isAntiAlias = true
        isDither = true
    },
    val bitmapPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG or Paint.FILTER_BITMAP_FLAG),

    var archPaintAdapter: WillPaintAdapter? = null,
    var textPaintAdapter: WillPaintAdapter? = null,
    var overlayTextPaintAdapter: WillPaintAdapter? = null,
    var separationArchPaintAdapter: WillPaintAdapter? = null,
    var bitmapPaintAdapter: WillPaintAdapter? = null
)