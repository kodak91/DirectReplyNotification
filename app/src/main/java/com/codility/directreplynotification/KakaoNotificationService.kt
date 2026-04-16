package com.codility.directreplynotification

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import com.google.gson.Gson

class KakaoNotificationService : NotificationListenerService() {

    companion object {
        const val KAKAO_PACKAGE = "com.kakao.talk"
        var wsServer: ChatWebSocketServer? = null
    }

    override fun onCreate() {
        super.onCreate()
        wsServer = ChatWebSocketServer(8887)
        wsServer?.start()
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        if (sbn.packageName != KAKAO_PACKAGE) return
        val extras = sbn.notification.extras
        val sender = extras.getString(Notification.EXTRA_TITLE) ?: return
        val message = extras.getCharSequence(Notification.EXTRA_TEXT)?.toString() ?: return
        val roomKey = sbn.key

        sbn.notification.actions
            ?.firstOrNull { it.remoteInputs?.isNotEmpty() == true }
            ?.let { ActionCache.save(roomKey, it) }

        val payload = Gson().toJson(mapOf(
            "type" to "message",
            "sender" to sender,
            "message" to message,
            "roomKey" to roomKey
        ))
        wsServer?.broadcast(payload)
    }

    override fun onDestroy() {
        super.onDestroy()
        wsServer?.stop()
    }
}
