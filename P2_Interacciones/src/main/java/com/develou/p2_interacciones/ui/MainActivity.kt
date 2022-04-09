package com.develou.p2_interacciones.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.os.bundleOf
import com.develou.p2_interacciones.data.BookmarksStore
import com.develou.p2_interacciones.R
import com.develou.p2_interacciones.receiver.BookmarkReceiver

const val GROUP_ID = 100

class MainActivity : AppCompatActivity() {
    private lateinit var CHANNEL_ID: String
    private val GROUP_POSTS_KEY = "post_group"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CHANNEL_ID = getString(R.string.basic_channel_id)

        findViewById<Button>(R.id.notification_button).setOnClickListener {
            generateNotification(
                1,
                "1. Crear notificaciones",
                getString(R.string.post_big_text)
            )
            generateNotification(
                2,
                "2. Interacciones de notificaciones",
                getString(R.string.post2_big_text)
            )
            groupNotifications()
        }

        createNotificationChannel()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            findViewById<TextView>(R.id.bookmarks_text).apply {
                text = getString(R.string.bookmarks, BookmarksStore.bookmarks)
            }
        }
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

    private fun generateNotification(
        notificationId: Int,
        title: String,
        description: String
    ) {
        val largeIcon = BitmapFactory.decodeResource(
            resources,
            R.drawable.develou
        )

        val contentIntent = Intent(this, PostDetailActivity::class.java).apply {
            putExtras(
                bundleOf(
                    "notification_id" to notificationId,
                    "title" to title,
                    "body" to description
                )
            )
        }
        val contentPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(contentIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val style = NotificationCompat.BigTextStyle().bigText(description)

        val bookmarkIntent = Intent(applicationContext, BookmarkReceiver::class.java).apply {
            putExtra("notification_id", notificationId)
        }
        val bookmarkPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationId,
            bookmarkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setLargeIcon(largeIcon)
            .setSmallIcon(R.drawable.ic_circle_notifications)
            .setContentTitle(title)
            .setContentText(description)
            .setSubText("Develou.com")
            .setStyle(style)
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.drawable.ic_bookmark, "Leer más tarde", bookmarkPendingIntent)
            .setGroup(GROUP_POSTS_KEY)
            .build()

        showNotification(notificationId, notification)
    }

    private fun groupNotifications() {
        val group = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_circle_notifications)
            .setSubText("Develou.com")
            .setGroup(GROUP_POSTS_KEY)
            .setGroupSummary(true)
            .build()

        showNotification(GROUP_ID, group)
    }

    private fun showNotification(notificationId: Int, notification: Notification) {
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notification)
        }
    }
}