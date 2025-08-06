package umc.onairmate.data.socket.handler

import org.json.JSONObject
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.socket.BaseSocketHandler
import umc.onairmate.data.socket.listener.ChatEventListener
import umc.onairmate.data.util.parseJson

class ChatHandler(
    private val listener: ChatEventListener
) : BaseSocketHandler {

    override fun handle(type: String, data: JSONObject) {
        when (type) {
            "receiveRoomMessage" -> listener.onNewChat(parseJson<ChatMessageData>(data))
            "BOOKMARK_CREATED" -> listener.onBookmarkCreated(parseJson<ChatMessageData>(data))
            "error" -> listener.onError(parseJson<String>(data))
            "userJoined" -> listener.onUserJoined(parseJson<String>(data))
        }
    }
}