package dev.remod.remodlauncher

import android.webkit.WebView
import android.annotation.SuppressLint
import android.app.*
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import dev.remod.remodlauncher.js_bridges.*

open class BrontoActivity : Activity() {

    private var webView: WebView? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView = findViewById(R.id.wv)
        setContentView(R.layout.activity_main)

        // Settings
        webView?.settings?.javaScriptEnabled = true

        webView?.addJavascriptInterface(Brontodroid(this), "Brontodroid")
        webView?.addJavascriptInterface(MiscInfoInterface(this), "MiscInfo")

        // Init after everything loads
        webView?.loadUrl("file:///android_asset/index.html")
    }

    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            super.onBackPressed()
        }
    }
}