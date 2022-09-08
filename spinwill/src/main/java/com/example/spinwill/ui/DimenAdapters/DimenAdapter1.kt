package com.example.spinwill.ui.DimenAdapters

import com.example.spinwill.models.WillDimenProperties
import com.example.spinwill.models.WillPaintProperties

interface OffSetDimenAdapter1 {

    fun getVOffsetOverLayText(
        tempAngle: Float,
        sweepAngle: Float,
        text: String,
        itemsSize: Int,
        paintProps: WillPaintProperties,
        dimenProps: WillDimenProperties
    ): Int

    fun getHOffsetOverlayText(
        tempAngle: Float,
        sweepAngle: Float,
        text: String,
        itemsSize: Int,
        paintProps: WillPaintProperties,
        dimenProps: WillDimenProperties
    ): Int
}