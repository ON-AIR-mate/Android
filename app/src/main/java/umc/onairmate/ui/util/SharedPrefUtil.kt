package umc.onairmate.ui.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import umc.onairmate.OnAirMateApplication
import androidx.core.content.edit

object SharedPrefUtil {
    val spf: SharedPreferences by lazy {
        OnAirMateApplication.applicationContext()
            .getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }
    // 저장 (Parcelize 객체 → JSON)
    fun <T> saveData(key: String, data: T) {
        val json = Gson().toJson(data)
        spf.edit { putString(key, json) }
    }

    // 불러오기 (JSON → Parcelize 객체)
    inline fun <reified T> getData( key: String): T? {
        val json = spf.getString(key, null) ?: return null
        return Gson().fromJson(json, T::class.java)
    }

    // 데이터 삭제
    fun removeData( key: String) {
        spf.edit { remove(key) }
    }

    // 전체 삭제
    fun clearAll() {
        spf.edit { clear() }
    }
}