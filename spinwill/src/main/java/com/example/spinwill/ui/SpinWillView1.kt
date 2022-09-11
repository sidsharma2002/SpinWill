package com.example.spinwill.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.spinwill.R
import com.example.spinwill.ui.adapters.WillItemUiAdapter

class SpinWillView1<T> constructor(
    context: Context
) : ConstraintLayout(context) {

    private lateinit var willView1: WillView1<T>

    init {
        val parentView =
            LayoutInflater.from(context).inflate(R.layout.layout_spinwillview1, null, false)
        willView1 = WillView1<T>(context, null)
        val parentLayout: FrameLayout = parentView.findViewById(R.id.container)
        parentLayout.addView(willView1)
        addView(parentView)
        postInvalidate()
    }

    fun setItems(data: List<T>) {
        willView1.setItems(data)
        willView1.invalidate()
    }

    fun setItemAdapter(willItemUiAdapter: WillItemUiAdapter<T>) {
        willView1.setItemAdapter(willItemUiAdapter)
        willView1.invalidate()
    }

    fun getWillView(): WillView1<T> {
        return willView1
    }

    fun setTextColor(@IdRes color: Int) {
        willView1.paintProps.textPaint.color = color
    }
}