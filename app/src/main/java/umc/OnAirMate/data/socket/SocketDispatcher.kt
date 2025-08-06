package umc.onairmate.data.socket

import android.util.Log
import io.socket.client.Socket
import org.json.JSONObject

object SocketDispatcher {
    private val handlers = mutableMapOf<String, BaseSocketHandler>()

    fun startListening(socket: Socket) {
        socket.on("receiveRoomMessage") { args ->
            Log.d("catch","data ${args}")
            if (args.isNotEmpty() && args[0] is JSONObject) {
                val json = args[0] as JSONObject
                val data = json.getJSONObject("data")
                dispatch( "receiveRoomMessage",data)
            }
        }
        socket.on("error") { args ->
            if (args.isNotEmpty()) {
                val data = args[0] as JSONObject
                val message = data.optString("message")
                Log.e("SocketError", "Error received: $message")
            }
        }

        socket.on("userJoined") { args ->
            Log.d("catch","data ${args}")
            if (args.isNotEmpty() && args[0] is JSONObject) {
                val json = args[0] as JSONObject
                val data = json.getJSONObject("data")
                dispatch( "userJoined",data)
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