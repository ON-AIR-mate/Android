package umc.onairmate.data.socket

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketManager {
    private lateinit var socket: Socket

    fun init(url: String, token: String) {
        val opts = IO.Options().apply {
            extraHeaders = mapOf("Authorization" to listOf("Bearer $token"))
        }
        socket = IO.socket(url, opts)
    }

    fun connect() {
        if (!socket.connected()) socket.connect()
    }

    fun disconnect() {
        socket.off()
        if (socket.connected()) socket.disconnect()
    }

    fun emit(eventType: String, data: JSONObject) {
        socket.emit(eventType, data)
    }

    fun getSocket(): Socket = socket
}
