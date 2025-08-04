package umc.onairmate.data.socket

import io.socket.client.IO
import io.socket.client.Socket

object SocketManager {
    private lateinit var socket: Socket

    fun initSocket(url: String, token: String) {
        val opts = IO.Options().apply {
            extraHeaders = mapOf("Authorization" to listOf("Bearer $token"))
            reconnection = true
        }
        socket = IO.socket(url, opts)
    }

    fun getSocket(): Socket = socket

    fun connect() {
        if (!socket.connected()) socket.connect()
    }

    fun disconnect() {
        if (socket.connected()) socket.disconnect()
    }
}