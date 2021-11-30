package dev.remod.remodlauncher.js_bridges

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import dev.remod.remodlauncher.BrontoActivity
import dev.remod.remodlauncher.R

class Brontodroid (var ctx: Context) {
    @JavascriptInterface
    fun showToast(toast: String?, length: Int) {
        val lengthVal = if (length == 0) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
        Toast.makeText(ctx, toast, lengthVal).show()
    }

    @JavascriptInterface
    fun changeStatusBarColor(r: Int, g: Int, b: Int) {
        var activity = ctx as Activity
        val window = activity.window
        window.statusBarColor = Color.rgb(r, g, b)
    }

//    @JavascriptInterface
//    fun buildNativeNotification(title: String, text: String, icon: String?) {
//        var notificationManager: NotificationManager
//        var notificationChannel: NotificationChannel
//        var builder: Notification.Builder
//
//        var channelId = "com.remod.brontodroid.notifs"
//
//        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val intent = Intent(ctx, BrontoActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            notificationChannel = NotificationChannel(channelId, text, NotificationManager.IMPORTANCE_HIGH)
//            notificationChannel.enableLights(true)
//            notificationChannel.lightColor = Color.GREEN
//            notificationChannel.enableVibration(false)
//            notificationManager.createNotificationChannel(notificationChannel)
//
//            builder = Notification.Builder(ctx, channelId)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setLargeIcon(BitmapFactory.decodeResource(ctx.resources, R.drawable.ic_launcher_background))
//                .setContentIntent(pendingIntent)
//        } else {
//
//            builder = Notification.Builder(ctx)
//                .setSmallIcon(R.drawable.ic_launcher_background)
//                .setLargeIcon(BitmapFactory.decodeResource(ctx.resources, R.drawable.ic_launcher_background))
//                .setContentIntent(pendingIntent)
//        }
//        notificationManager.notify(1234, builder.build())
//    }
}