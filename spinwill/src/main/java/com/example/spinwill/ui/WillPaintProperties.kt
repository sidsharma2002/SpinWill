package com.example.spinwill.ui

import android.graphics.Paint

data class WillPaintProperties(
    val archPaint: Paint = Paint(),
    val textPaint: Paint = Paint(),
    val overlayTextPaint: Paint = Paint(),
    val separationArchPaint: Paint = Paint()
)