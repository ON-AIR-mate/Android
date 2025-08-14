package umc.onairmate.data.socket.handler

import android.util.Log
import org.json.JSONObject
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.SocketMessage
import umc.onairmate.data.socket.SocketHandler
import umc.onairmate.data.socket.listener.ChatRoomEventListener
import umc.onairmate.data.util.parseJson

class ChatRoomHandler(
    private val listener: ChatRoomEventListener
) : SocketHandler {

    override fun getEventMap(): Map<String, (JSONObject) -> Unit> {
        return mapOf(
            "receiveRoomMessage" to { data ->
                val parsed = parseJson<ChatMessageData>(data)
                if (parsed == null) {
                    val error =  SocketMessage(type = "receiveRoomMessage", message = data.toString())
                    listener.onError(error)
                }
                else listener.onNewChat(parsed)
            },
            "error" to { data ->
                val parsed = parseJson<SocketMessage>(data)
                if (parsed == null) {
                    Log.w("ChatRoomHandler", "SocketError 파싱 실패: $data")
                }
                // 파싱 실패 시 기본 에러 객체 생성
                val safeError = parsed ?: SocketMessage(type = "JSON_PARSE_ERROR", message = data.toString())
                listener.onError(safeError)
            },
            "success" to { data ->
                val parsed = parseJson<SocketMessage>(data)
                if (parsed == null) {
                    val error =  SocketMessage(type = "JSON_PARSE_ERROR", message = data.toString())
                    listener.onError(error)
                }
                else listener.onSuccess(parsed)
            },
            "userJoined" to { data ->
                listener.onUserJoined(true)
            },
            "userLeft" to { data ->
                listener.onUserLeft(true)
            },
            "roomSettingsUpdated" to {data ->
                val parsed = parseJson<RoomData>(data)
                if (parsed == null) {
                    val error =  SocketMessage(type = "roomSettingsUpdated", message = data.toString())
                    listener.onError(error)
                }
                else listener.onRoomSettingsUpdated(parsed)
            }
        )
    }
}