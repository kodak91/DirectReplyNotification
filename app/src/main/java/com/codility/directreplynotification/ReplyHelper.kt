package com.codility.directreplynotification

import android.app.RemoteInput
import android.content.Context
import android.content.Intent
import android.os.Bundle

object ReplyHelper {
    fun sendReply(context: Context, roomKey: String, text: String): Boolean {
        val action = ActionCache.get(roomKey) ?: return false
        val remoteInput = action.remoteInputs?.firstOrNull() ?: return false
        return try {
            val intent = Intent()
            RemoteInput.addResultsToIntent(
                action.remoteInputs,
                intent,
                Bundle().apply { putString(remoteInput.resultKey, text) }
            )
            action.actionIntent.send(context, 0, intent)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
