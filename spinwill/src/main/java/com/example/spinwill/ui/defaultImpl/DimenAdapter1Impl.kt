package com.example.spinwill.ui.defaultImpl

import com.example.spinwill.models.WillDimenProperties
import com.example.spinwill.models.WillPaintProperties
import com.example.spinwill.ui.DimenAdapters.OffSetDimenAdapter1


class OverlayDimenAdapter1Impl : OffSetDimenAdapter1 {
    override fun getHOffsetOverlayText(
        tempAngle: Float,
        sweepAngle: Float,
        text: String,
        itemsSize: Int,
        paintProps: WillPaintProperties,
        dimenProps: WillDimenProperties
    ): Int {
        val textWidth: Float = paintProps.overlayTextPaint.measureText(text)
        return (dimenProps.radius * Math.PI / itemsSize - textWidth / 2).toInt()
    }

    override fun getVOffsetOverLayText(
        tempAngle: Float,
        sweepAngle: Float,
        text: String,
        itemsSize: Int,
        paintProps: WillPaintProperties,
        dimenProps: WillDimenProperties
    ): Int {
        return dimenProps.radius / 4 - 5
    }
}

class TextDimenAdapter1Impl : OffSetDimenAdapter1 {
    override fun getHOffsetOverlayText(
        tempAngle: Float,
        sweepAngle: Float,
        text: String,
        itemsSize: Int,
        paintProps: WillPaintProperties,
        dimenProps: WillDimenProperties
    ): Int {
        val textWidth: Float = paintProps.overlayTextPaint.measureText(text)
        val h = (2 * dimenProps.radius * Math.PI / itemsSize - textWidth / 2).toInt()
        return h / 2
    }

    override fun getVOffsetOverLayText(
        tempAngle: Float,
        sweepAngle: Float,
        text: String,
        itemsSize: Int,
        paintProps: WillPaintProperties,
        dimenProps: WillDimenProperties
    ): Int {
        return dimenProps.radius / 5 - 3
    }
}