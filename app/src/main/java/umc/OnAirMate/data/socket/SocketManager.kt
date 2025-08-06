package umc.onairmate.data.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketManager {
    private val TAG = javaClass.simpleName
    private lateinit var socket: Socket
    private var initialized = false

    fun init(url: String, token: String) {
        Log.d(TAG, "token ${token}")
        val t = "a3cf432329261a87130b1ffe0c751534a8da127ca2dc2a8080930a0fd55c6fde"
        val opts = IO.Options().apply {
            auth = mapOf("token" to t)
        }
        socket = IO.socket(url, opts)
        initialized = true

    }

    fun connect() {
        if (!initialized) return

        socket.on(Socket.EVENT_CONNECT) {
            Log.d("SocketManager", "Socket connected")
        }
        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d("SocketManager", "Socket disconnected")
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e("SocketManager", "Socket connect error: ${args.joinToString()}")
        }

        socket.connect()
    }

    fun disconnect() {
        if (!initialized) return
        socket.disconnect()
        socket.off() // 모든 리스너 제거
        Log.d("SocketManager", "Socket manually disconnected")
    }

    fun emit(eventType: String, data: JSONObject) {
        Log.d(TAG, "emit ${eventType} / data ${data}")
        socket.emit(eventType, data)
    }

    fun getSocket(): Socket = socket

    fun isInitialized() : Boolean = initialized
}
