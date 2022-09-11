package com.example.spinwill.ui

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.spinwill.R
import com.example.spinwill.adapter.WillPaintAdapter
import com.example.spinwill.models.WillDimenProperties
import com.example.spinwill.models.WillGradient
import com.example.spinwill.models.WillPaintProperties
import com.example.spinwill.ui.DimenAdapters.OffSetDimenAdapter1
import com.example.spinwill.ui.adapters.WillItemUiAdapter
import com.example.spinwill.ui.defaultImpl.OverlayDimenAdapter1Impl
import com.example.spinwill.ui.defaultImpl.TextDimenAdapter1Impl
import com.example.spinwill.utils.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * * note : this cannot be inflated directly from xml.
 */
class WillView1<T> constructor(
    context: Context,
    attributeSet: AttributeSet?
) : View(context, attributeSet) {

    companion object {
        const val DEF_BORDER_PERCENT = 3f
    }

    private var wheelSize: Int = 0

    /**
     * list for the will-data items
     */
    private lateinit var items: List<T>

    fun setItems(items: List<T>) {
        this.items = items
        wheelSize = items.size
    }

    /**
     * adapter for will-data items.
     */
    private lateinit var itemAdapter: WillItemUiAdapter<T>

    fun setItemAdapter(adapter: WillItemUiAdapter<T>) {
        itemAdapter = adapter
    }

    private val dimenProps = WillDimenProperties()
    val paintProps = WillPaintProperties()
    val gradientProps = WillGradient()
    private var willRange = RectF()
    private var mPath = Path()
    private var mRect = Rect()
    private var mMatrix = Matrix()

    private var overlayDimenAdapter1: OffSetDimenAdapter1 = OverlayDimenAdapter1Impl()

    fun setOverLayDimenAdapter1(adapter: OffSetDimenAdapter1) {
        overlayDimenAdapter1 = adapter
    }

    private var textDimenAdapter1: OffSetDimenAdapter1 = TextDimenAdapter1Impl()

    fun setTextDimenAdapter1(adapter: OffSetDimenAdapter1) {
        textDimenAdapter1 = adapter
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas == null || ::items.isInitialized.not() || ::itemAdapter.isInitialized.not()) return

        var tempAngle = 0f
        val sweepAngle: Float = 360f / wheelSize
        val rotateAngle = sweepAngle / 2 + 90

        for (i in items.indices) {
            // drawPieBackgroundBorder(canvas)
            drawPieBackground(i, tempAngle, sweepAngle, canvas)

            val bitmap = itemAdapter.getRewardBitmap(items[i])
            if (bitmap != null) {
                drawImage(canvas, tempAngle, itemAdapter.getRewardBitmap(items[i])!!, rotateAngle)
            }

            drawOverlayText(
                canvas,
                tempAngle,
                sweepAngle,
                itemAdapter.getOverlayText(items[i])
            )
            drawText(
                canvas,
                tempAngle,
                sweepAngle,
                itemAdapter.getRewardText(items[i])
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

    private fun drawImage(canvas: Canvas, tempAngle: Float, bitmap: Bitmap, rotateAngle: Float) {
        // get every arc img width and angle
        val imgWidth: Int = getImageWidth()
        val angle: Float = ((tempAngle + 360 / wheelSize / 2) * Math.PI / 180).toFloat()

        val x: Int =
            (dimenProps.center + 5 * (dimenProps.radius) / 9 * cos(angle.toDouble())).toInt()
        val y: Int =
            (dimenProps.center + 5 * (dimenProps.radius) / 9 * sin(angle.toDouble())).toInt()

        // create arc to draw
        mRect.set(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2)

        // rotate main bitmap
        val px: Float = mRect.exactCenterX()
        val py: Float = mRect.exactCenterY()
        val ar = bitmap.width.toFloat() / bitmap.height

        mMatrix.postTranslate(
            (-bitmap.width shr 1).toFloat(),
            (-bitmap.height shr 1).toFloat()
        )
        mMatrix.postScale(
            imgWidth.toFloat() / bitmap.width,
            imgWidth.toFloat() / ar / bitmap.height
        )
        mMatrix.postRotate(tempAngle + rotateAngle)
        mMatrix.postTranslate(px, py)
        canvas.drawBitmap(bitmap, mMatrix, paintProps.bitmapPaint)
        mMatrix.reset()
        mRect.setEmpty()
    }

    private fun getImageWidth(): Int {
        val sweepAngle: Double = 360.0 / wheelSize
        val angle = Math.toRadians((180 - sweepAngle) / 2)

        return (2f * (dimenProps.radius) / 4 * cos(angle)).toInt()
    }


    private fun drawOverlayText(
        canvas: Canvas,
        tempAngle: Float,
        sweepAngle: Float,
        text: String
    ) {
        mPath.addArc(willRange, tempAngle, sweepAngle)
        val hOffset: Int = overlayDimenAdapter1.getHOffsetOverlayText(
            tempAngle,
            sweepAngle,
            text,
            wheelSize,
            paintProps,
            dimenProps
        )
        val vOffset: Int = overlayDimenAdapter1.getVOffsetOverLayText(
            tempAngle,
            sweepAngle,
            text,
            wheelSize,
            paintProps,
            dimenProps
        )
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

        val hOffset: Int = textDimenAdapter1.getHOffsetOverlayText(
            tempAngle,
            sweepAngle,
            text,
            wheelSize,
            paintProps,
            dimenProps
        )

        val vOffset: Int = textDimenAdapter1.getVOffsetOverLayText(
            tempAngle,
            sweepAngle,
            text,
            wheelSize,
            paintProps,
            dimenProps
        )

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

        val padding = (width * DEF_BORDER_PERCENT / 100).toInt()

        dimenProps.let {
            it.paddingLeft = padding // paddingLeft
            it.paddingBottom = padding // paddingBottom
            it.paddingTop = padding // paddingTop
            it.paddingRight = padding // paddingRight
            it.padding = padding
        }

        dimenProps.radius = (width - (dimenProps.paddingLeft + dimenProps.paddingRight)) / 2
        dimenProps.center = width / 2

        setMeasuredDimension(
            width,
            width
        ) // hence the size for the view will be min(w,h) i.e square

        initComponents()
    }

    private fun initComponents() {
        if (paintProps.archPaintAdapter != null) {
            paintProps.archPaintAdapter!!.setPaintProperties(paintProps.archPaint)
        } else {
            paintProps.archPaint.apply {
                color = ContextCompat.getColor(context, R.color.coin_txt)
                strokeWidth = 0.01f
            }
        }

        if (paintProps.textPaintAdapter != null) {
            paintProps.textPaintAdapter!!.setPaintProperties(paintProps.textPaint)
        } else {
            paintProps.textPaint.apply {
                typeface = ResourcesCompat.getFont(
                    context,
                    R.font.montserrat_extrabolditalic
                )
                textSize = (dimenProps.radius * 1.5 / 9).toFloat()
            }
        }

        if (paintProps.overlayTextPaintAdapter != null) {
            paintProps.overlayTextPaintAdapter!!.setPaintProperties(paintProps.overlayTextPaint)
        } else {
            paintProps.overlayTextPaint.apply {
                color = ContextCompat.getColor(
                    context,
                    R.color.spinwill_overalytext_color
                )
                typeface = ResourcesCompat.getFont(
                    context,
                    R.font.montserrat_extrabolditalic
                )
                textSize = (dimenProps.radius * 3 / 9).toFloat()
            }
        }

        if (paintProps.separationArchPaintAdapter != null) {
            paintProps.separationArchPaintAdapter!!.setPaintProperties(paintProps.separationArchPaint)
        } else {
            paintProps.separationArchPaint.apply {
                color = ContextCompat.getColor(
                    context,
                    R.color.spinwill_arc_seperation_line
                )
                strokeWidth = dimenProps.radius / 55f
            }
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