package dev.remod.remodlauncher

import android.app.Activity
import android.webkit.WebViewClient
import android.webkit.WebView
import android.content.Intent
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.webkit.JavascriptInterface
import android.widget.Toast
import dev.remod.remodlauncher.Deno.fs

open class BrontoActivity : Activity() {
    internal inner class CustomWV : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            val hostname = "google.com"
            val uri = Uri.parse(url)
            if (url.startsWith("file:") || uri.host != null && uri.host!!.endsWith(hostname)) {
                return false
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            view.context.startActivity(intent)
            return true
        }
    }

    private var wv: WebView? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        wv = findViewById(R.id.activity_main_webview)
        val ws = wv?.getSettings()
        ws?.javaScriptEnabled = true
        wv?.setWebViewClient(CustomWV())

        class Brontodroid (var ctx: Context) {
            @JavascriptInterface
            fun showToast(toast: String?, length: Int) {
                val lengthVal = if (length == 0) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
                Toast.makeText(ctx, toast, lengthVal).show()
            }

            @JavascriptInterface
            fun changeStatusBarColor(r: Int, g: Int, b: Int) {
                val window = window
                window.statusBarColor = Color.rgb(r, g, b)
            }

        }
        wv?.loadUrl("file:///android_asset/index.html")
        wv?.addJavascriptInterface(Brontodroid(this), "Brontodroid")
        wv?.addJavascriptInterface(Deno.fs(this), "Deno")
    }

    override fun onBackPressed() {
        if (wv!!.canGoBack()) {
            wv!!.goBack()
        } else {
            super.onBackPressed()
        }
    }
}