package com.develou.p2_interacciones.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.develou.p2_interacciones.data.BookmarksStore
import com.develou.p2_interacciones.ui.GROUP_ID

private var bookmark1 = false
private var bookmark2 = false

class BookmarkReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        BookmarksStore.addBookmark()
        val notificationId = intent.getIntExtra("notification_id", -1)

        /*
         Usamos dos centinelas para determinar cuando se debe desvanecer el grupo de notificaciones.
         Sin embargo, debes crear un proceso de administración personalizado que controle de forma
         general este aspecto
         */
        if (notificationId == 1) {
            if(bookmark1)
                bookmark2 = false
            else
                bookmark1 = true
        }

        if (notificationId == 2) {
            if (bookmark2)
                bookmark1 = false
            else
                bookmark2 = true
        }

        with(NotificationManagerCompat.from(context)) {
            cancel(notificationId)
            if (bookmark1 && bookmark2)
                cancel(GROUP_ID)
        }
    }
}