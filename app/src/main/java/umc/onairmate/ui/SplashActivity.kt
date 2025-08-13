package umc.onairmate.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.ui.login.LoginActivity
import umc.onairmate.R
import androidx.core.content.edit

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val splashHandler = android.os.Handler(android.os.Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val spf = this.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val autoLogin = if (spf.contains("auto_login")) spf.getBoolean("auto_login", false) else false

        //타이머 종료 후 내부 실행
        splashHandler.postDelayed(Runnable {
            //앱의 loginActivity로 넘어가기
            // 토큰이 있다면 자동로그인 -> 메인으로 넘어가기
            val intent =  if(autoLogin)  Intent(this@SplashActivity, MainActivity::class.java)
            else Intent(this@SplashActivity, LoginActivity::class.java)

            startActivity(intent)
            //현재 activity 닫기
            finish()
        }, 700) //0.7sec
    }
}