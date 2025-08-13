package umc.onairmate.data.socket.handler

import android.util.Log
import org.json.JSONObject
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.DirectMessageData
import umc.onairmate.data.model.entity.SocketError
import umc.onairmate.data.socket.SocketHandler
import umc.onairmate.data.socket.listener.FriendEventListener
import umc.onairmate.data.util.parseJson

class FriendHandler(
    private val listener: FriendEventListener
) : SocketHandler {
    override fun getEventMap(): Map<String, (JSONObject) -> Unit> {
        return mapOf(
            "receiveDirectMessage" to {data ->
                Log.d("FriendHandler","data ${data}")
                val parsed = parseJson<DirectMessageData>(data)
                if (parsed == null) {
                    val error =  SocketError(type = "receiveDirectMessage", message = data.toString())
                    listener.onError(error)
                }
                else listener.onNewDirectMessage(parsed)

            },
            "error" to { data ->
                val parsed = parseJson<SocketError>(data)
                if (parsed == null) {
                    Log.w("ChatRoomHandler", "SocketError 파싱 실패: $data")
                }
                // 파싱 실패 시 기본 에러 객체 생성
                val safeError = parsed ?: SocketError(type = "JSON_PARSE_ERROR", message = data.toString())
                listener.onError(safeError)
            }
        )
    }
}