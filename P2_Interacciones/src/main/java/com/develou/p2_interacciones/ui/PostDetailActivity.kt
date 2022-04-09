package com.develou.p2_interacciones.ui

import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.develou.p2_interacciones.R

class PostDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        discardNotifications()
        setUpContent()
    }

    private fun discardNotifications() {
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun setUpContent() {
        val title = intent.getStringExtra("title")
        val body = intent.getStringExtra("body")

        findViewById<TextView>(R.id.post_category).text = "Android > UI > Notificaciones"
        findViewById<TextView>(R.id.post_body).text = body
        findViewById<TextView>(R.id.post_title).text = title
    }
}