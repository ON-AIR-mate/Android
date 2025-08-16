package umc.onairmate.data.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketManager {
    private val TAG = javaClass.simpleName
    private var socket: Socket? = null

    @Synchronized
    fun init(url: String, token: String) {
        val opts = IO.Options().apply {
            auth = mapOf("token" to token)
        }
        socket = IO.socket(url, opts)

    }

    // 연결
    @Synchronized
    fun connect() {
        val currentSocket = socket ?: return
        if (currentSocket.connected()) return
        currentSocket.off() // 중복 연결 방지
        currentSocket.on(Socket.EVENT_CONNECT) {
            Log.d(TAG, "Socket connected")
            SocketDispatcher.reregisterAll(currentSocket)
        }
        currentSocket.on(Socket.EVENT_DISCONNECT) {
            Log.d(TAG, "Socket disconnected")
        }
        currentSocket.on(Socket.EVENT_CONNECT_ERROR) { args ->
            Log.e(TAG, "Socket connect error: ${args.joinToString()}")
        }

        currentSocket.connect()
    }

    // 연결 해제
    fun disconnect() {
        try {
            socket?.let {
                it.disconnect()
                it.off()
                Log.d(TAG, "Socket manually disconnected")
            } ?: Log.w(TAG, "Socket is null - no action taken")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to disconnect socket", e)
        }
    }


    // 전송
    fun emit(eventType: String, data: JSONObject) {
        val currentSocket = socket
        if (currentSocket == null || !currentSocket.connected()) {
            Log.w(TAG, "Socket not connected, cannot emit $eventType")
            return
        }
        Log.d(TAG, "emit ${eventType} : ${data}")
        currentSocket.emit(eventType, data)
    }

    fun getSocketOrNull(): Socket? = socket

    fun getSocket(): Socket? = socket

}
