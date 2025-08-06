package umc.onairmate.data.socket

import io.socket.client.Socket
import org.json.JSONObject

object SocketDispatcher {
    private val activeHandlers = mutableListOf<SocketHandler>()

    /** 핸들러 등록 (필요 시점에 호출) */
    fun registerHandler(socket: Socket, handler: SocketHandler) {
        activeHandlers.add(handler)
        handler.getEventMap().forEach { (eventName, callback) ->
            socket.on(eventName) { args ->
                if (args.isNotEmpty() && args[0] is JSONObject) {
                    callback(args[0] as JSONObject)
                }
            }
        }
    }


    fun unregisterHandler(socket: Socket, handler: SocketHandler) {
        handler.getEventMap().keys.forEach { eventName ->
            socket.off(eventName)
        }
        activeHandlers.remove(handler)
    }



    /** 모든 핸들러 해제 (필요 시 전체 초기화용) */
    fun clearAllHandlers(socket: Socket) {
        activeHandlers.forEach { handler ->
            handler.getEventMap().keys.forEach { socket.off(it) }
        }
        activeHandlers.clear()
    }
}