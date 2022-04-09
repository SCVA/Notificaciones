package com.develou.notificaciones_en_android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.notification_button).setOnClickListener {
            generateNotification()
        }

        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {   //(1)
            val name = getString(R.string.basic_channel_name)   // (2)
            val channelId = getString(R.string.basic_channel_id) // (3)
            val descriptionText = getString(R.string.basic_channel_description) // (4)
            val importance = NotificationManager.IMPORTANCE_DEFAULT // (5)

            val channel = NotificationChannel(channelId, name, importance).apply { // (6)
                description = descriptionText
            }

            val nm: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager // (7)
            nm.createNotificationChannel(channel) // (8)
        }
    }

    private fun generateNotification() {
        val notificationId = 0
        val channelId = getString(R.string.basic_channel_id) // (1)
        val largeIcon = BitmapFactory.decodeResource( // (2)
            resources,
            R.drawable.develou
        )

        val notification = NotificationCompat.Builder(this, channelId) // (3)
            .setLargeIcon(largeIcon) // (4)
            .setSmallIcon(R.drawable.ic_circle_notifications) // (5)
            .setContentTitle("Guía De Notificaciones En Android") // (6)
            .setContentText("1. Crear Notificaciones") // (7)
            .setSubText("Develou.com") // (8)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // (9)
            .addAction(R.drawable.ic_bookmark, "Leer más tarde", null) // (10)
            .build()

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification)
        }
    }
}