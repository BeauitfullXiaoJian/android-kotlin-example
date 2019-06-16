package com.example.androidx_example.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs

import kotlinx.android.synthetic.main.fragment_web.*
import com.example.androidx_example.R


/**
 * 一个浏览器例子，用于载入指定网页
 *
 */
class WebFragment : Fragment() {

    private val args: WebFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initWebView()
    }

    override fun onPause() {
        super.onPause()
        webViewSaveBundle = Bundle()
        web_view.saveState(webViewSaveBundle)
    }

    private fun initWebView() {
        web_view.apply {
            settings.allowUniversalAccessFromFileURLs = true
            settings.allowFileAccessFromFileURLs = true
            settings.allowContentAccess = true
            settings.domStorageEnabled = true
            // settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    web_swipe?.isRefreshing = false
                    load_bar?.progress = newProgress
                    load_bar?.visibility = if (newProgress >= 100) View.INVISIBLE else View.VISIBLE
                }
            }

            webViewSaveBundle?.apply { web_view.restoreState(webViewSaveBundle) } ?: loadUrl(args.webUrl)
        }

        web_swipe?.apply {
            setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
            setOnRefreshListener {
                load_bar.visibility = View.VISIBLE
                web_view.loadUrl(web_view.url)
            }
            setOnChildScrollUpCallback { _, _ ->
                web_view.scrollY > 0
            }
        }
    }

    companion object {
        var webViewSaveBundle: Bundle? = null
    }
}
