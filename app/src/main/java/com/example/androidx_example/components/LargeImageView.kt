package com.example.androidx_example.components

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.example.androidx_example.R
import com.example.androidx_example.until.tool.debugInfo
import java.io.InputStream
import java.net.URL

/**
 * 大图加载视图，这个视图还未编写！！！！！
 */
class LargeImageView : View {

    private lateinit var mMoveGestureDetector: GestureDetector
    private var mBitmapRegionDecoder: BitmapRegionDecoder? = null
    var _imageSrc: String? = null

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.LargeImageView, defStyle, 0
        )
        _imageSrc = a.getString(
            R.styleable.LargeImageView_imageSrc
        )
        a.recycle()
        invalidateBitmapRegionDecoder()
        initMoveGesture()
    }

    override fun onDraw(canvas: Canvas) {
        val options = BitmapFactory.Options().apply {
            // 设置这个参数为true,代表我们不需要加载图片到内存，而只获取它的基本信息
            inJustDecodeBounds = true
            inSampleSize = 2
        }
        BitmapFactory.decodeResource(
            resources,
            R.drawable.splash,
            options
        )
        debugInfo("图片尺寸:${options.outWidth},${options.outHeight}") // 图片尺寸:450,732
//        canvas.drawBitmap(bmp, 0f, 0f, null)
//        bmp.recycle()
//        val bmp = mBitmapRegionDecoder?.let {
//            getRectBitmap(it)
//        }
//        bmp?.let {
//            canvas.drawBitmap(it, 0f, 0f, null)
//        }
    }

    private fun invalidateBitmapRegionDecoder() {
        //mBitmapRegionDecoder =
//        _imageSrc?.let {
//            GlobalScope.launch(Dispatchers.IO) {
//                val inputStream = createNetworkImageStream(it)
//                withContext(Dispatchers.Main) {
//                    mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false)
//                }
//            }
//        }
    }

    fun initMoveGesture() {
        mMoveGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                start: MotionEvent?,
                current: MotionEvent?,
                distanceX: Float,
                distanceY: Float
            ): Boolean {

                return true
            }
        })
    }

    companion object {

        fun downloadImage(context: Context, imageUrl: String) {
            // GlideApp.with(context).load(imageUrl)
        }

        fun createBitmapRegionDecoder(inputStream: InputStream) {
            val brd = BitmapRegionDecoder.newInstance(inputStream, false)
        }

        fun createNetworkImageStream(urlStr: String): InputStream? {
            return URL(urlStr).openConnection().getInputStream()
        }

        fun getRectBitmap(brd: BitmapRegionDecoder): Bitmap {
            return brd.decodeRegion(Rect(0, 0, 40, 40), BitmapFactory.Options())
        }
    }
}
