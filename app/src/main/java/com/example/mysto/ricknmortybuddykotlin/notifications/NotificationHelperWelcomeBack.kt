package com.example.mysto.ricknmortybuddykotlin.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.provider.Settings
import androidx.core.app.NotificationCompat
import com.example.mysto.ricknmortybuddykotlin.R
import com.example.mysto.ricknmortybuddykotlin.mainActivity.MainActivity


class NotificationHelperWelcomeBack(private val mContext: Context) {
    private var mNotificationManager: NotificationManager? = null
    private var mBuilder: NotificationCompat.Builder? = null
    private val NOTIFICATION_CHANNEL_ID = "10001"

    /**
     * Create and push the notification
     */
    fun createNotification(title: String, message: String) {
        /**Creates an explicit intent for an Activity in your app */
        val resultIntent = Intent(mContext, MainActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val resultPendingIntent =
            PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        mBuilder = NotificationCompat.Builder(mContext, NOTIFICATION_CHANNEL_ID)
        mBuilder!!.setSmallIcon(R.drawable.notifications)
            .setLargeIcon(BitmapFactory.decodeResource(mContext.resources, R.drawable.notifications))
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(false)
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setContentIntent(resultPendingIntent)

        mNotificationManager = mContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager?

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.MAGENTA
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            assert(mNotificationManager != null)
            mBuilder!!.setChannelId(NOTIFICATION_CHANNEL_ID)
            mNotificationManager!!.createNotificationChannel(notificationChannel)
        }
        assert(mNotificationManager != null)
        mNotificationManager!!.notify(0, mBuilder!!.build())
    }
}