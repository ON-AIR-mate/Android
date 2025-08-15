package umc.onairmate.data.socket.handler

import android.util.Log
import org.json.JSONObject
import umc.onairmate.data.model.entity.SocketError
import umc.onairmate.data.model.entity.VideoPauseData
import umc.onairmate.data.model.entity.VideoPlayData
import umc.onairmate.data.model.entity.VideoSyncData
import umc.onairmate.data.socket.SocketHandler
import umc.onairmate.data.socket.listener.VideoEventListener
import umc.onairmate.data.util.parseJson

class RoomHandler(
    private val listener: VideoEventListener
): SocketHandler {
    override fun getEventMap(): Map<String, (JSONObject) -> Unit> {
        return mapOf(
            "video:sync" to { data ->
                Log.d("RoomHandler","sync ${data}")
                val parsed = parseJson<VideoSyncData>(data)
                if (parsed == null) {
                    val error = SocketError(type = "receiveVideoSync", message = data.toString())
                    listener.onError(error)
                }
                else listener.syncVideo(parsed)
            },
            "video:play" to { data ->
                Log.d("RoomHandler","play ${data}")
                val parsed = parseJson<VideoPlayData>(data)
                if (parsed == null) {
                    val error = SocketError(type = "receiveVideoPlay", message = data.toString())
                    listener.onError(error)
                }
                else listener.onVideoPlay(parsed)
            },
            "video:pause" to { data ->
                Log.d("RoomHandler","pause ${data}")
                val parsed = parseJson<VideoPauseData>(data)
                if (parsed == null) {
                    val error = SocketError(type = "receiveVideoPause", message = data.toString())
                    listener.onError(error)
                }
                else listener.onVideoPause(parsed)
            },
            "error" to { data ->
                Log.d("RoomHandler","error ${data}")
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




