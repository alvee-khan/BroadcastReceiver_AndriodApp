package com.example.broadcastreceiver

import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.widget.AppCompatImageView
import kotlin.math.min

class ZoomableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val zoomMatrix = Matrix()

    private var currentZoom = 1f

    private val minZoom = 0.75f
    private val maxZoom = 4f

    private var lastX = 0f
    private var lastY = 0f

    private val scaleDetector = ScaleGestureDetector(
        context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

            override fun onScale(
                detector: ScaleGestureDetector
            ): Boolean {

                val newZoom = (
                        currentZoom * detector.scaleFactor
                        ).coerceIn(minZoom, maxZoom)

                val factor = newZoom / currentZoom

                zoomMatrix.postScale(
                    factor,
                    factor,
                    detector.focusX,
                    detector.focusY
                )

                currentZoom = newZoom

                correctBounds()

                imageMatrix = zoomMatrix

                return true
            }
        }
    )

    init {
        scaleType = ScaleType.MATRIX
        isClickable = true
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)

        post {
            resetZoom()
        }
    }

    override fun onSizeChanged(
        w: Int,
        h: Int,
        oldw: Int,
        oldh: Int
    ) {
        super.onSizeChanged(w, h, oldw, oldh)

        resetZoom()
    }

    fun resetZoom() {

        val image = drawable ?: return

        val imageWidth = image.intrinsicWidth.toFloat()
        val imageHeight = image.intrinsicHeight.toFloat()

        if (
            width <= 0 ||
            height <= 0 ||
            imageWidth <= 0 ||
            imageHeight <= 0
        ) {
            return
        }

        val fitScale = min(
            width / imageWidth,
            height / imageHeight
        )

        val scaledWidth = imageWidth * fitScale
        val scaledHeight = imageHeight * fitScale

        val dx = (width - scaledWidth) / 2f
        val dy = (height - scaledHeight) / 2f

        zoomMatrix.reset()

        zoomMatrix.postScale(
            fitScale,
            fitScale
        )

        zoomMatrix.postTranslate(dx, dy)

        currentZoom = 1f

        imageMatrix = zoomMatrix
    }

    private fun correctBounds() {

        val image = drawable ?: return

        val rect = RectF(
            0f,
            0f,
            image.intrinsicWidth.toFloat(),
            image.intrinsicHeight.toFloat()
        )

        zoomMatrix.mapRect(rect)

        val deltaX = when {

            rect.width() <= width ->
                width / 2f - rect.centerX()

            rect.left > 0f ->
                -rect.left

            rect.right < width ->
                width - rect.right

            else -> 0f
        }

        val deltaY = when {

            rect.height() <= height ->
                height / 2f - rect.centerY()

            rect.top > 0f ->
                -rect.top

            rect.bottom < height ->
                height - rect.bottom

            else -> 0f
        }

        zoomMatrix.postTranslate(
            deltaX,
            deltaY
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (drawable == null) {
            return super.onTouchEvent(event)
        }

        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            parent?.requestDisallowInterceptTouchEvent(true)
        }

        scaleDetector.onTouchEvent(event)

        when (event.actionMasked) {

            MotionEvent.ACTION_DOWN -> {

                lastX = event.x
                lastY = event.y
            }

            MotionEvent.ACTION_MOVE -> {

                if (
                    !scaleDetector.isInProgress &&
                    event.pointerCount == 1 &&
                    currentZoom > 1f
                ) {

                    val dx = event.x - lastX
                    val dy = event.y - lastY

                    zoomMatrix.postTranslate(dx, dy)

                    correctBounds()

                    imageMatrix = zoomMatrix
                }

                lastX = event.x
                lastY = event.y
            }

            MotionEvent.ACTION_POINTER_UP -> {

                val remainingIndex =
                    if (event.actionIndex == 0) 1 else 0

                if (remainingIndex < event.pointerCount) {

                    lastX = event.getX(remainingIndex)
                    lastY = event.getY(remainingIndex)
                }
            }

            MotionEvent.ACTION_UP -> {

                performClick()

                parent?.requestDisallowInterceptTouchEvent(false)
            }

            MotionEvent.ACTION_CANCEL -> {

                parent?.requestDisallowInterceptTouchEvent(false)
            }
        }

        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
