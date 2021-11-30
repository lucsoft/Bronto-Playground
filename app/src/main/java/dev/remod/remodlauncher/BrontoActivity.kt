package dev.remod.remodlauncher

import android.webkit.WebView
import android.annotation.SuppressLint
import android.app.*
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import dev.remod.remodlauncher.js_bridges.*

open class BrontoActivity : Activity() {

    private var webView: WebView = findViewById(R.id.wv)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Settings
        webView.settings.javaScriptEnabled = true

        // Preload Bridges and Evals
        webView.webViewClient = WebChromeClient() as WebViewClient
        webView.addJavascriptInterface(Brontodroid(this), "Brontodroid")
        webView.addJavascriptInterface(MiscInfoInterface(this), "MiscInfo")

        webView.evaluateJavascript("let Deno = {}\n" +
                "\n" +
                "Deno.build = {\n" +
                "    target: MiscInfo.getTarget(),\n" +
                "    arch: MiscInfo.getArch(),\n" +
                "    os: MiscInfo.getOS(),\n" +
                "    vendor: MiscInfo.getVendor(),\n" +
                "    env: MiscInfo.getEnv()\n" +
                "}", {
                value -> println("Loaded")
        })

        // Init after everything loads
        webView.loadUrl("file:///android_asset/index.html")
    }

    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            super.onBackPressed()
        }
    }
}