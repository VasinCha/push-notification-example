package com.example.test_fcm_push_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.UUID

const val channelId = "notification_channel"
const val channelName = "test_channel"

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        // Receive notification message from FCM server when application is running in foreground or background.
        // If application isn't running, this method isn't invoked but Push Notification bar will create by default FCM library instead.
        Log.i("notification","@@@@ onMessageReceived invoked when App is running only")
        super.onMessageReceived(message)
        var data : Map<String, String>? = null
        if (message.notification !==null){
            Log.i("notification","@@@@ ${message.notification!!.title!!}")
            Log.i("notification","@@@@ ${message.notification!!.body!!}")
            if (message.data.isNotEmpty()) {
                data = message.data
                var page = data["target_page"]
                val key1 = data["key_1"]
                val key2 = data["key_2"]
                Log.i("notification","@@@@ page: $page, key_1: $key1, key_2: $key2")
            }

            sendCustomNotification(message)
        }
    }
    private fun sendCustomNotification(remoteMessage: RemoteMessage) {
        // Example Payload from FCM Server
        // {
        //      "to": "device_token",
        //      "data": {
        //          "target_page": "second_page",
        //          "key_1": "This is value Key 1",
        //          "key_2": "This is value Key 2"
        //      },
        //      "notification": {
        //          "title": "Notification Title",
        //          "body": "Notification Body"
        //      }
        // }

        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.data

            // Build intent to launch the app and pass the page information
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("title", remoteMessage.notification!!.title!!)
            intent.putExtra("body", remoteMessage.notification!!.body!!)
            intent.putExtra("target_page", data["target_page"])
            intent.putExtra("key_1", data["key_1"])
            intent.putExtra("key_2", data["key_2"])
            intent.action = UUID.randomUUID().toString()
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // PendingIntent to launch the app
            val pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

            // Build the notification
            val notificationBuilder = NotificationCompat.Builder(this, "default_channel_id")
                .setSmallIcon(androidx.loader.R.drawable.notification_icon_background)
                .setContentTitle(remoteMessage.notification?.title)
                .setContentText(remoteMessage.notification?.body)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel("default_channel_id", "Default Channel", NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}