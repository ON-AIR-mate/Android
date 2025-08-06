package umc.onairmate.data.socket.handler

import org.json.JSONObject
import umc.onairmate.data.socket.SocketHandler
import umc.onairmate.data.socket.listener.ChatRoomEventListener
import umc.onairmate.data.util.parseJson

class ChatRoomHandler(
    private val listener: ChatRoomEventListener
) : SocketHandler {

    override fun getEventMap(): Map<String, (JSONObject) -> Unit> {
        return mapOf(
            "receiveRoomMessage" to { data ->
                listener.onNewChat(parseJson(data))
            },
            "error" to { data ->
                listener.onError(parseJson(data))
            },
            "userJoined" to { data ->
                //listener.onUserJoined(parseJson(data))
            },
            "leaveRoom" to { data ->
                //listener.onUserLeft(parseJson(data))
            }
        )
    }
}