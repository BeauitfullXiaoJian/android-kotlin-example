package com.example.androidx_example.fragments.dir_explorer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import com.example.androidx_example.R
import com.example.androidx_example.until.tool.RxUntil
import com.example.androidx_example.until.ui.AnimateUntil
import com.example.androidx_example.until.ui.ViewUntil
import com.github.barteksc.pdfviewer.PDFView
import java.net.URL

class PdfWindow(
    pdfUrl: String,
    rootView: ViewGroup
) :
    PopupWindow() {

    init {
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.MATCH_PARENT
        isFocusable = true
        contentView = LayoutInflater.from(rootView.context).inflate(
            R.layout.dir_explorer_pdf_window,
            rootView,
            false
        )
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        AnimateUntil.popup(contentView)
        initPadView(pdfUrl)
    }

    private fun initPadView(pdfUrl: String) {
        val pdfView = contentView.findViewById<PDFView>(R.id.pdf_view)
        val loadingView = contentView.findViewById<ImageView>(R.id.pdf_loading_view)
        loadingView.setImageDrawable(ViewUntil.getAnimationDrawable(contentView.context))
        val disposable = RxUntil.mainObs { URL(pdfUrl).openStream() }.subscribe { stream ->
            pdfView.fromStream(stream).onLoad { loadingView.visibility = View.GONE }.load()
        }
        setOnDismissListener { disposable.dispose() }
    }

    companion object {
        fun createAndShow(pdfUrl: String, rootView: ViewGroup) {
            PdfWindow(pdfUrl, rootView)
                .showAtLocation(rootView, Gravity.CENTER, rootView.x.toInt(), rootView.y.toInt())
        }
    }
}