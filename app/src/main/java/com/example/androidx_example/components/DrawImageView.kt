package com.example.androidx_example.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Size
import android.view.*
import android.widget.ImageView
import androidx.dynamicanimation.animation.FlingAnimation
import androidx.dynamicanimation.animation.FloatValueHolder
import com.example.androidx_example.until.debugInfo

/**
 * 涂鸦
 */
class DrawImageView : ImageView {

    private lateinit var mMoveGestureDetector: GestureDetector
    private lateinit var mScaleGestureDetector: ScaleGestureDetector


    var activeColor: Int = 0
    var drawBitmap: Bitmap? = null
        set(value) {
            drawBitmap?.recycle()
            field = value
            invalidate()
        }
    private var activeDrawLineData: DrawLineData? = null
    private var drawLineRows = arrayListOf<DrawLineData>()
    private val drawMatrix = Matrix()
    private var fitSize = Size(0, 0)
    private var fitPoint = PointF()
    private var fitRect = Rect()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        setBackgroundColor(Color.GRAY)
        initMoveGesture()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        imageFitCenter()
    }


    override fun onDraw(canvas: Canvas) {
        canvas.concat(drawMatrix)
        drawBitmap?.also {
            canvas.drawBitmap(it, null, fitRect, null)
        }
        for (line in drawLineRows) {
            drawLine(canvas, line)
        }
        activeDrawLineData?.also {
            drawLine(canvas, it)
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        // 获取坐标
        val x = event.x
        val y = event.y

        // 如果有两个或以上手指，那么不绘制
        if (event.pointerCount > 1) {
            mScaleGestureDetector.onTouchEvent(event)
            mMoveGestureDetector.onTouchEvent(event)
            return true
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                activeDrawLineData = newDrawLineData(activeColor).apply {
                    paint.strokeWidth /= getMatrixScale(drawMatrix)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val floatPoint = floatArrayOf(x, y)
                val mtx = Matrix()
                drawMatrix.invert(mtx)
                mtx.mapPoints(floatPoint)
                activeDrawLineData?.points?.add(PointF(floatPoint.first(), floatPoint.last()))
            }
            MotionEvent.ACTION_UP -> {
                mMoveGestureDetector.onTouchEvent(event)
                drawLineRows.add(activeDrawLineData!!)
                activeDrawLineData = null
            }
        }
        invalidate()
        return true
    }


    private fun initMoveGesture() {
        mMoveGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                start: MotionEvent?,
                current: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                drawMatrix.postTranslate(-distanceX, -distanceY)
                invalidate()
                return true
            }


            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                debugInfo("我的坐标", velocityX.toString())
                val flingX = FlingAnimation(FloatValueHolder())
                val flingY = FlingAnimation(FloatValueHolder())
                var beforeValueX = 0f
                var beforeValueY = 0f
                // val vX = Math.max(velocityX, if (velocityX > 0) 1000f else -1000f)
                // val vY = Math.max(velocityY, if (velocityY > 0) 1000f else -1000f)
                flingX.addUpdateListener { _, value, _ ->
                    drawMatrix.postTranslate(value - beforeValueX, 0f)
                    beforeValueX = value
                    invalidate()
                }
                flingY.addUpdateListener { _, value, _ ->
                    drawMatrix.postTranslate(0f, value - beforeValueY)
                    beforeValueY = value
                }
                flingX.setStartVelocity(velocityX)
                    .setFriction(1.8f)
                    .start()
                flingY.setStartVelocity(velocityY)
                    .setFriction(1.8f)
                    .start()
                return true
            }
        })
        mScaleGestureDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.OnScaleGestureListener {
            override fun onScaleBegin(detector: ScaleGestureDetector?) = true
            override fun onScaleEnd(detector: ScaleGestureDetector?) {}


            override fun onScale(detector: ScaleGestureDetector): Boolean {
                drawMatrix.postScale(
                    detector.scaleFactor,
                    detector.scaleFactor,
                    detector.focusX,
                    detector.focusY
                )
                invalidate()
                return true
            }
        })
    }

    private fun getMatrixPoint(matrix: Matrix): PointF {
        val mtrValues = FloatArray(9)
        matrix.getValues(mtrValues)
        val x = mtrValues[Matrix.MTRANS_X]
        val y = mtrValues[Matrix.MTRANS_Y]
        return PointF(x, y)
    }

    private fun getMatrixScale(matrix: Matrix): Float {
        val mtrValues = FloatArray(9)
        matrix.getValues(mtrValues)
        return mtrValues[Matrix.MSCALE_X]
    }


    private fun drawLine(canvas: Canvas, lineData: DrawLineData) {
        for (i in 0 until lineData.points.size - 1) {
            val startPoint = lineData.points[i]
            val endPoint = lineData.points[i + 1]
            canvas.drawLine(
                startPoint.x,
                startPoint.y,
                endPoint.x,
                endPoint.y,
                lineData.paint
            )
        }
    }

    private fun imageFitCenter() {
        drawBitmap?.also {
            fitSize = getScaleFitSize(
                containerSize = Size(width, height),
                originSize = Size(drawBitmap!!.width, drawBitmap!!.height)
            )
            fitPoint = getChildCenterPoint(
                parentSize = Size(width, height),
                childSize = fitSize
            )
            fitRect = Rect(fitPoint.x.toInt(), fitPoint.y.toInt(), fitSize.width, fitSize.height)
        }
    }

    fun getSnapshotBitmap(): Bitmap? {
        return drawBitmap?.let { sourceBmp ->
            val snapshot = Bitmap.createBitmap(sourceBmp.width, sourceBmp.height, Bitmap.Config.ARGB_8888)
            Canvas(snapshot).run {
                drawBitmap(sourceBmp, 0f, 0f, null)
                for (line in drawLineRows) {
                    drawLine(this, line)
                }
                activeDrawLineData?.also {
                    drawLine(this, it)
                }
            }
            return@let snapshot
        }
    }

    fun redo() {
        val size = this.drawLineRows.size
        if (size > 0) {
            this.drawLineRows.removeAt(size - 1)
            invalidate()
        }
    }

    /**
     * 每条线的数据
     */
    data class DrawLineData(
        val points: ArrayList<PointF>,
        val paint: Paint
    )

    companion object {

        private fun newDrawLineData(paintColor: Int): DrawLineData {
            val drawPaint = Paint().apply {
                color = paintColor
                isAntiAlias = true
                strokeJoin = Paint.Join.ROUND
                strokeCap = Paint.Cap.ROUND
                strokeWidth = 10f
            }
            return DrawLineData(arrayListOf(), drawPaint)
        }

        private fun getChildCenterPoint(parentSize: Size, childSize: Size): PointF {
            return PointF(
                (parentSize.width - childSize.width) / 2f,
                (parentSize.height - childSize.height) / 2f
            )
        }

        /**
         * 计算一个适合面板的缩放尺寸
         */
        private fun getScaleFitSize(containerSize: Size, originSize: Size): Size {
            // 我们把视频尺寸缩放为容器宽度,高度按视频比例缩放
            val cwOW = 1f * containerSize.width / originSize.width
            val expW = containerSize.width
            val expH = (originSize.height * cwOW).toInt()
            return if (expH < containerSize.height)
                Size(expW, expH)
            else return Size((containerSize.height * (1f * expW / expH)).toInt(), containerSize.height)
        }
    }
}
