package umc.onairmate

import android.annotation.SuppressLint
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.DefaultLifecycleObserver
import dagger.hilt.android.HiltAndroidApp
import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import umc.onairmate.data.socket.SocketManager

@HiltAndroidApp
class OnAirMateApplication : Application(), DefaultLifecycleObserver {

    companion object{
        @SuppressLint("StaticFieldLeak")
        private lateinit var context: Context

        fun getString(@StringRes stringResId: Int): String{
            return context.getString(stringResId)
        }

        @SuppressLint("StaticFieldLeak")
        private lateinit var instance: OnAirMateApplication
        fun applicationContext(): Context{
            return instance.applicationContext
        }
    }

    override fun onCreate() {
        super<Application>.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        context = applicationContext

        // 앱 상태 감지 시작
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
    override fun onStart(owner: LifecycleOwner) {
        // 앱 포그라운드 복귀 시 소켓 재연결
        SocketManager.connect()
    }

    override fun onStop(owner: LifecycleOwner) {
        // 앱 백그라운드 진입 시 소켓 해제
        SocketManager.disconnect()
    }


    // 토큰 받아오기
    private fun getToken(): String?{
        val spf = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return spf.getString("access_token", null)
    }
}