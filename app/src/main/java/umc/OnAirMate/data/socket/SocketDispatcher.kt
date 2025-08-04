package umc.onairmate.data.socket

import io.socket.client.Socket
import org.json.JSONObject

object SocketDispatcher {
    private val handlers = mutableMapOf<String, BaseSocketHandler>()

    fun startListening(socket: Socket) {
        socket.on("event") { args ->
            if (args.isNotEmpty() && args[0] is JSONObject) {
                val json = args[0] as JSONObject
                val type = json.getString("type")
                val data = json.getJSONObject("data")
                dispatch(type, data)
            }
        }
    }

    fun register(type: String, handler: BaseSocketHandler) {
        handlers[type] = handler
    }

    fun unregister(type: String) {
        handlers.remove(type)
    }

    private fun dispatch(type: String, data: JSONObject) {
        handlers[type]?.handle(type, data)
    }

    fun clear() {
        handlers.clear()
    }
}