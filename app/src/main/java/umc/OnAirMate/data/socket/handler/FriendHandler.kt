package umc.onairmate.data.socket.handler

import org.json.JSONObject
import umc.onairmate.data.model.entity.DirectMessageData
import umc.onairmate.data.socket.SocketHandler
import umc.onairmate.data.socket.listener.FriendEventListener
import umc.onairmate.data.util.parseJson

class FriendHandler(
    private val listener: FriendEventListener
) : SocketHandler {
    override fun getEventMap(): Map<String, (JSONObject) -> Unit> {
        return mapOf(
            "receiveDirectMessage" to {data ->
                listener.onNewDirectMessage(parseJson<DirectMessageData>(data))
            },
            "error" to { data ->
                listener.onError(parseJson<String>(data))
            }
        )
    }
}