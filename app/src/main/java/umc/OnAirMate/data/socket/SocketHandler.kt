package umc.onairmate.data.socket

import org.json.JSONObject

interface SocketHandler {
    fun getEventMap(): Map<String, (JSONObject) -> Unit>
}