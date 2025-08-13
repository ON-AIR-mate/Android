package umc.onairmate.data.util

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

inline fun <reified T> parseJson(json: JSONObject): T? {
    return try {
        // 최상단에 "data" 랩퍼가 있으면 내부만 사용, 없으면 전체 사용
        val targetObj = json.optJSONObject("data") ?: json
        Gson().fromJson<T>(
            targetObj.toString(),
            object : TypeToken<T>() {}.type
        )
    } catch (e: Exception) {
        Log.d("ErrorParser", "parseJson error: ${e.message}")
        null
    }
}