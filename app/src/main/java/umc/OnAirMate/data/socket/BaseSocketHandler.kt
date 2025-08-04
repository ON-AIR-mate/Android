package umc.onairmate.data.socket

import org.json.JSONObject

interface BaseSocketHandler {
    fun handle(type: String, data: JSONObject)
}