package umc.onairmate.data.socket.handler

import android.util.Log
import org.json.JSONObject
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.SocketError
import umc.onairmate.data.socket.SocketHandler
import umc.onairmate.data.socket.listener.ChatRoomEventListener
import umc.onairmate.data.util.parseJson

class ChatRoomHandler(
    private val listener: ChatRoomEventListener
) : SocketHandler {

    override fun getEventMap(): Map<String, (JSONObject) -> Unit> {
        return mapOf(
            "receiveRoomMessage" to { data ->
                listener.onNewChat(parseJson<ChatMessageData>(data))
            },
            "error" to { data ->
                val parsed = parseJson<SocketError>(data)
                if (parsed == null) {
                    Log.w("ChatRoomHandler", "SocketError 파싱 실패: $data")
                }
                // 파싱 실패 시 기본 에러 객체 생성
                val safeError = parsed ?: SocketError(type = "JSON_PARSE_ERROR", message = data.toString())
                listener.onError(safeError)
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