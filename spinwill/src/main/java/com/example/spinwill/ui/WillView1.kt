package com.example.spinwill.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.spinwill.R
import com.example.spinwill.utils.dp
import kotlin.math.min

class WillView1 constructor(
    context: Context,
    attributeSet: AttributeSet?
) : View(context, attributeSet) {

    companion object {
        const val DEFAULT_PADDING = 5
    }

    private val dimenProps = WillDimenProperties()
    private val paintProps = WillPaintProperties()
    private val gradientProps = WillGradient()
    private var willRange = RectF()
    private var mPath = Path()

    private val wheelSize = 8 // TODO for testing

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null) return

        var tempAngle = 0f
        val sweepAngle: Float = 360f / wheelSize
        val rotateAngle = sweepAngle / 2 + 90

        for (i in 0..7) {
            // draw graphics
            drawPieBackground(i, tempAngle, sweepAngle, canvas)
            // drawImage(canvas, tempAngle, mWheelItems.get(i).bitmap, rotateAngle)
            drawOverlayText(
                canvas,
                tempAngle,
                sweepAngle,
                i.toString()
            )
            drawText(
                canvas,
                tempAngle,
                sweepAngle,
                i.toString()
            )
            drawSeparationArc(willRange, tempAngle, sweepAngle, canvas)
            // prepare for next iteration
            tempAngle += sweepAngle
            paintProps.archPaint.reset()
        }
    }

    private fun drawPieBackground(i: Int, tempAngle: Float, sweepAngle: Float, canvas: Canvas) {
        if (i % 2 == 0) paintProps.archPaint.shader = gradientProps.darkYellow
        if (i % 2 == 1) paintProps.archPaint.shader = gradientProps.lightYellow
        canvas.drawArc(willRange, tempAngle, sweepAngle, true, paintProps.archPaint)
    }

    private fun drawOverlayText(
        canvas: Canvas,
        tempAngle: Float,
        sweepAngle: Float,
        text: String
    ) {
        mPath.addArc(willRange, tempAngle, sweepAngle)
        val textWidth: Float = paintProps.overlayTextPaint.measureText(text)
        val hOffset: Int = (dimenProps.radius * Math.PI / wheelSize - textWidth / 2).toInt()
        val vOffset: Int = dimenProps.radius / 4 - 5
        canvas.drawTextOnPath(
            text,
            mPath,
            hOffset.toFloat(),
            vOffset.toFloat(),
            paintProps.overlayTextPaint
        )
        mPath.reset()
    }

    private fun drawText(canvas: Canvas, tempAngle: Float, sweepAngle: Float, text: String) {
        mPath.addArc(willRange, tempAngle, sweepAngle) //used global Path
        val textWidth: Float = paintProps.textPaint.measureText(text)
        val hOffset: Int = (dimenProps.radius * Math.PI / wheelSize - textWidth / 2).toInt()
        // change this '5' number to change the radial distance of text from the center
        // change this '5' number to change the radial distance of text from the center
        val vOffset: Int = dimenProps.radius / 5 - 3
        paintProps.textPaint.color = ContextCompat.getColor(context, R.color.spinwill_text_color)
        canvas.drawTextOnPath(
            text,
            mPath,
            hOffset.toFloat(),
            vOffset.toFloat(),
            paintProps.textPaint
        )
        mPath.reset()
    }

    private fun drawSeparationArc(
        range: RectF,
        tempAngle: Float,
        sweepAngle: Float,
        canvas: Canvas
    ) {
        canvas.drawArc(range, tempAngle, sweepAngle, true, paintProps.separationArchPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = min(measuredWidth, measuredHeight)

        dimenProps.padding =
            if (paddingLeft == 0) DEFAULT_PADDING else paddingLeft
        dimenProps.radius = (width - (dimenProps.padding * 2)) / 2
        dimenProps.center = width / 2

        setMeasuredDimension(
            width,
            width
        ) // hence the size for the view will be min(w,h) i.e square

        initComponents()
    }

    private fun initComponents() {
        paintProps.archPaint.apply {
            isAntiAlias = true
            isDither = true
            color = ContextCompat.getColor(context, R.color.coin_txt)
            strokeWidth = 0.01f
            style = Paint.Style.FILL_AND_STROKE
        }

        paintProps.textPaint.apply {
            isAntiAlias = true
            isDither = true
            typeface = ResourcesCompat.getFont(
                context,
                R.font.montserrat_extrabolditalic
            )
            textSize = 20.dp()
            letterSpacing = 0.1f
        }

        paintProps.overlayTextPaint.apply {
            color = ContextCompat.getColor(
                context,
                R.color.spinwill_overalytext_color
            )
            isAntiAlias = true
            isDither = true
            typeface = ResourcesCompat.getFont(
                context,
                R.font.montserrat_extrabolditalic
            )
            textSize = 45.dp()
            letterSpacing = 0.1f
        }

        paintProps.separationArchPaint.apply {
            style = Paint.Style.STROKE
            color = ContextCompat.getColor(
                context,
                R.color.spinwill_arc_seperation_line
            )
            isAntiAlias = true
            isDither = true
            strokeWidth = 3f
        }

        val padding = dimenProps.padding.toFloat()
        val diameter = dimenProps.radius * 2
        willRange = RectF(padding, padding, padding + diameter, padding + diameter)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        gradientProps.darkYellow = RadialGradient(
            w / 2f, h / 2f,
            h / 3.5f,
            ContextCompat.getColor(context, R.color.spinwill_darkyellow_gradientLight),
            ContextCompat.getColor(context, R.color.spinwill_darkyellow_gradientDark),
            Shader.TileMode.CLAMP
        )
        gradientProps.lightYellow = RadialGradient(
            w / 2f, h / 2f,
            h / 2.6f,
            ContextCompat.getColor(context, R.color.white),
            ContextCompat.getColor(context, R.color.spinwil_lightyellow_gradientDark),
            Shader.TileMode.CLAMP
        )
    }
}