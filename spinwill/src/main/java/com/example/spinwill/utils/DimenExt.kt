package com.example.spinwill.utils

import android.content.res.Resources
import android.util.TypedValue

fun Int.dp(): Float {
    val dip = this
    val r: Resources = Resources.getSystem()
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dip.toFloat(),
        r.displayMetrics
    )
}