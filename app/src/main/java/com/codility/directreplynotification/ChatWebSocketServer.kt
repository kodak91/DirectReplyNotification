package com.codility.directreplynotification

import com.google.gson.Gson
import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class ChatWebSocketServer(port: Int) : WebSocketServer(InetSocketAddress(port)) {
    override fun onOpen(conn: WebSocket, handshake: ClientHandshake) {}
    override fun onMessage(conn: WebSocket, message: String) {
        val json = Gson().fromJson(message, Map::class.java)
        if (json["type"] == "reply") {
            val roomKey = json["roomKey"] as String
            val text = json["text"] as String
            ReplyHelper.sendReply(App.context, roomKey, text)
        }
    }
    override fun onClose(conn: WebSocket, code: Int, reason: String, remote: Boolean) {}
    override fun onError(conn: WebSocket, ex: Exception) { ex.printStackTrace() }
    override fun onStart() {}
}
