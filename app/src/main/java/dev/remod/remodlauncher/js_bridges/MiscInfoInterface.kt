package dev.remod.remodlauncher.js_bridges

import android.content.Context
import android.webkit.JavascriptInterface

class MiscInfoInterface(ctx: Context) {
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