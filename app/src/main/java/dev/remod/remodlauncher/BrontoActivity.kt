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
import android.util.Log
import android.view.Window
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.widget.Toast
import java.util.logging.Logger

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

        // Client Init
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        wv = findViewById(R.id.activity_main_webview)
        wv?.setWebViewClient(CustomWV())

        // Settings
        val ws = wv?.getSettings()
        ws?.javaScriptEnabled = true

        // Bronto momento
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

        // Misc stuff to call info in JS Context
        class Misc(var ctx: Context) {
            @JavascriptInterface
            fun getTarget(): String = "${getArch()}-${getVendor()}-${getOS()}-${getEnv()}"
            @JavascriptInterface
            fun getArch(): String = System.getProperty("os.arch")
            @JavascriptInterface
            fun getOS(): String = System.getProperty("os.name")
            @JavascriptInterface
            fun getVendor(): String = if (System.getProperty("os.arch").toString().endsWith("32")) "pc" else "unknown"
            @JavascriptInterface
            fun getEnv(): String = "gnu"
        }

        wv?.addJavascriptInterface(Brontodroid(this), "Brontodroid")

        wv?.addJavascriptInterface(Misc(this), "MiscInfo")
        wv?.evaluateJavascript("let Deno = {}\n" +
                "\n" +
                "Deno.build = {\n" +
                "    target: MiscInfo.getTarget(),\n" +
                "    arch: MiscInfo.getArch(),\n" +
                "    os: MiscInfo.getOS(),\n" +
                "    vendor: MiscInfo.getVendor(),\n" +
                "    env: MiscInfo.getEnv()\n" +
                "}", ValueCallback {
                    value -> println("Loaded")
        })
        wv?.addJavascriptInterface(Deno.fs(this), "Deno")

        /* To make sure all custom modules load
        before pageload and not end up causing
        issues with load and close scripts, we
        need to load page after modules load */
        wv?.loadUrl("file:///android_asset/index.html")
    }

    override fun onBackPressed() {
        if (wv!!.canGoBack()) {
            wv!!.goBack()
        } else {
            super.onBackPressed()
        }
    }
}