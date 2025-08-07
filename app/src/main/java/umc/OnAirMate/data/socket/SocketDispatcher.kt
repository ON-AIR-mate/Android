package umc.onairmate.data.socket

import android.util.Log
import io.socket.client.Socket
import org.json.JSONObject
import java.util.Collections

object SocketDispatcher {
    private val activeHandlers = mutableListOf<SocketHandler>().apply {
        Collections.synchronizedList(this)
    }
    /** 핸들러 등록 (필요 시점에 호출) */
    fun registerHandler(socket: Socket, handler: SocketHandler) {
        if (!activeHandlers.contains(handler)) {
            activeHandlers.add(handler)
        }

        handler.getEventMap().forEach { (eventName, callback) ->
            socket.off(eventName) // 먼저 기존 리스너 제거 (중복 방지)

            socket.on(eventName) { args ->
                if (args.isNotEmpty() && args[0] is JSONObject) {
                    try {
                        callback(args[0] as JSONObject)
                    } catch (e: Exception) {
                        Log.e("SocketDispatcher", "Error in event callback for $eventName", e)
                    }
                }
            }
        }
    }


    fun unregisterHandler(socket: Socket, handler: SocketHandler) {
        try {
            handler.getEventMap().keys.forEach { eventName ->
                socket.off(eventName)
            }
            activeHandlers.remove(handler)
        } catch (e: Exception) {
            Log.e("SocketDispatcher", "Failed to unregister handler", e)
        }
    }




    /** 모든 핸들러 해제 (필요 시 전체 초기화용) */
    fun clearAllHandlers(socket: Socket) {
        activeHandlers.forEach { handler ->
            handler.getEventMap().keys.forEach { socket.off(it) }
        }
        activeHandlers.clear()
    }
}