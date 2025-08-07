package umc.onairmate.data.util

import com.google.gson.Gson
import org.json.JSONObject

inline fun <reified T> parseJson(json: JSONObject): T {
    return Gson().fromJson(json.toString(), T::class.java)
}
