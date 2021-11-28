package dev.remod.remodlauncher

import android.webkit.WebViewClient
import android.webkit.WebView
import android.content.Intent
import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.webkit.JavascriptInterface
import android.webkit.ValueCallback
import android.widget.Toast

open class BrontoActivity : Activity() {

    private var wv: WebView? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Client Init
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        wv = findViewById(R.id.activity_main_webview)

        // Settings
        val ws = wv?.getSettings()
        ws?.javaScriptEnabled = true
        wv?.loadUrl("https://github.com")

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

            @JavascriptInterface
            fun buildNativeNotification(title: String, text: String, icon: String?) {
                var notificationManager: NotificationManager
                var notificationChannel: NotificationChannel
                var builder: Notification.Builder

                var channelId = "com.remod.brontodroid.notifs"

                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val intent = Intent(ctx, BrontoActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = NotificationChannel(channelId, text, NotificationManager.IMPORTANCE_HIGH)
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.GREEN
                    notificationChannel.enableVibration(false)
                    notificationManager.createNotificationChannel(notificationChannel)

                    builder = Notification.Builder(ctx, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(ctx.resources, R.drawable.ic_launcher_background))
                        .setContentIntent(pendingIntent)
                } else {

                    builder = Notification.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(BitmapFactory.decodeResource(ctx.resources, R.drawable.ic_launcher_background))
                        .setContentIntent(pendingIntent)
                }
                notificationManager.notify(1234, builder.build())
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
                "}", {
                    value -> println("Loaded")
        })

        /* To make sure all custom modules load
        before pageload and not end up causing
        issues with load and close scripts, we
        need to load page after modules load */

    }

    override fun onBackPressed() {
        if (wv!!.canGoBack()) {
            wv!!.goBack()
        } else {
            super.onBackPressed()
        }
    }
}