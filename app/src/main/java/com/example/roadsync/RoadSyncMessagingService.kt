
package com.example.roadsync

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class RoadSyncMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "roadsync_notifications"
        private const val CHANNEL_NAME = "RoadSync Notifications"

        private const val DEFAULT_TITLE = "RoadSync Update"
        private const val DEFAULT_BODY = "You have a new notification."
    }

    // ==========================================
    // RECEIVE FIREBASE CLOUD MESSAGE
    // ==========================================

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val title = message.notification?.title
            ?: message.data["title"]
            ?: DEFAULT_TITLE

        val body = message.notification?.body
            ?: message.data["body"]
            ?: DEFAULT_BODY

        showNotification(title, body)
    }

    // ==========================================
    // DISPLAY NOTIFICATION
    // ==========================================

    private fun showNotification(
        title: String,
        body: String
    ) {

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE)
                    as NotificationManager

        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
    }

    // ==========================================
    // CREATE NOTIFICATION CHANNEL
    // ==========================================

    private fun createNotificationChannel(
        notificationManager: NotificationManager
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for RoadSync updates."
            }

            notificationManager.createNotificationChannel(channel)
        }
    }
}
