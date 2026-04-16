package com.codility.directreplynotification

import android.app.Notification

object ActionCache {
    private val cache = mutableMapOf<String, Notification.Action>()
    fun save(roomKey: String, action: Notification.Action) { cache[roomKey] = action }
    fun get(roomKey: String) = cache[roomKey]
}
