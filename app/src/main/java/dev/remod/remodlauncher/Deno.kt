package dev.remod.remodlauncher

import android.content.Context
import android.webkit.JavascriptInterface
import java.io.File

class Deno {
    internal class fs(private val ctx: Context) {
        companion object {
            @JavascriptInterface
            fun ensureFile(path: File): Boolean {
                return path.exists() && !path.isDirectory && path.isFile
            }

            @JavascriptInterface
            fun ensureDir(path: File): Boolean {
                return path.exists() && path.isDirectory && !path.isFile
            }
        }
    }

    internal class os(private val ctx: Context)
}