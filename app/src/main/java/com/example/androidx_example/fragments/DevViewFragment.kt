package com.example.androidx_example.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.navigation.fragment.navArgs
import com.example.androidx_example.R
import com.example.androidx_example.components.DrawImageView
import com.example.androidx_example.components.FlingImageView
import com.example.androidx_example.components.ScaleImageView
import com.example.androidx_example.components.SpringImageView
import com.example.androidx_example.until.getTempBitmapUri
import com.example.androidx_example.until.getTempSaveFileOutputStream
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_dev_view.*

class DevViewFragment : BaseFragment() {

    private val args by navArgs<DevViewFragmentArgs>()
    private lateinit var devView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dev_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadDemoView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    /**
     * 初始化相关视图控件
     */
    private fun loadDemoView() {
        when (args.demoType) {
            DevDemoType.IMAGE_DRAW -> {
                val view = DrawImageView(context!!)
                devView = view
                view.activeColor = Color.RED
                view.drawBitmap = BitmapFactory.decodeResource(resources, R.drawable.splash)
                container_view.addView(view, 0)
                initDrawToolbar()
            }
            DevDemoType.IMAGE_FLING -> {
                val view = FlingImageView(context!!)
                view.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.bg_center))
                container_view.addView(view)
            }
            DevDemoType.IMAGE_SPRING -> {
                val view = SpringImageView(context!!)
                view.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.bg_center))
                container_view.addView(view)
            }
            DevDemoType.IMAGE_SCALE -> {
                val view = ScaleImageView(context!!)
                view.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.bg_center))
                container_view.addView(view)
            }
            DevDemoType.LARGE_IMAGE_LOAD -> {
            }
        }
    }

    private fun initDrawToolbar() {
        val dv = devView as DrawImageView
        draw_toolbar.visibility = View.VISIBLE
        draw_toolbar.children.forEach {
            it.isSaveEnabled = true
            when (it.tag) {
                getString(R.string.save) -> it.setOnClickListener {
                    dv.getSnapshotBitmap()?.also { bmp ->
                        val os = getTempSaveFileOutputStream(context!!, "jpg")
                        bmp.compress(Bitmap.CompressFormat.JPEG, 100, os)
                        os.close()
                        showToast("保存图片成功")
                    }
                }
                getString(R.string.redo) -> it.setOnClickListener {
                    dv.redo()
                }
                else -> it.setOnClickListener { target ->
                    dv.activeColor = mapOf(
                        "colorAccent" to ContextCompat.getColor(context!!, R.color.colorAccent),
                        "colorDanger" to ContextCompat.getColor(context!!, R.color.colorDanger),
                        "colorText" to ContextCompat.getColor(context!!, R.color.colorText)
                    )[target.tag.toString()] ?: 0
                    cleanAllSelected()
                    target.isSelected = true
                }
            }
        }
        draw_toolbar.children.first().performClick()
    }

    private fun cleanAllSelected() {
        draw_toolbar.children.forEach { it.isSelected = false }
    }

    enum class DevDemoType {
        IMAGE_DRAW,
        IMAGE_FLING,
        IMAGE_SPRING,
        IMAGE_SCALE,
        LARGE_IMAGE_LOAD,
    }
}
