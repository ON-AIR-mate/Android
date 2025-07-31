package umc.OnAirMate.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import umc.onairmate.R
import umc.onairmate.ui.MainActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //타이머 종료 후 내부 실행
        Handler().postDelayed(Runnable {
            //앱의 mainActivity로 넘어가기
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            //현재 activity 닫기
            finish()
        }, 5000) //5sec
    }
}