package umc.OnAirMate.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.onairmate.ui.join.JoinProfileFragment
import kotlinx.coroutines.launch
import umc.onairmate.data.model.request.TestRequest
import umc.onairmate.databinding.ActivityLoginBinding
import umc.onairmate.ui.login.LoginRequest
import umc.onairmate.ui.login.RetrofitClient


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //로그인 버튼 클릭 이벤트
        binding.btnLogin.setOnClickListener {
            val userId = binding.etId.text.toString()
            val userPw = binding.etPassword.text.toString()

            if (userId.isBlank() || userPw.isBlank()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            login(userId, userPw)
        }

        // 회원가입 화면 이동
        binding.newprofile.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(umc.onairmate.R.id.fragment_container, JoinProfileFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    /*
    private fun login(username: String, password: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    val loginData = response.body()!!.data!!
                    // TODO: 로그인 성공 후 토큰 저장...?
                    Toast.makeText(
                        this@LoginActivity,
                        "로그인 성공! ${loginData.user.nickname}님 환영합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "로그인 실패: 아이디 또는 비밀번호를 확인하세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "네트워크 오류: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

     */
    private fun login(username: String, password: String) {
        // TestRequest에 미리 넣어둔 임의 데이터
        val testUser = TestRequest(
            username = "testuser",
            password = "1234",
            nickname = "테스트닉네임",
            profileImage = "https://example.com/profile.jpg",
            agreements = TestRequest.Agreement()
        )

        // 입력값과 비교
        if (username == testUser.username && password == testUser.password) {
            // 로그인 성공 처리 (예: 토스트, 화면 전환 등)
            Toast.makeText(
                this,
                "로그인 성공! ${testUser.nickname}님 환영합니다.",
                Toast.LENGTH_SHORT
            ).show()

            // TODO: 필요시 메인 화면으로 이동하거나 토큰 저장 코드 추가
        } else {
            Toast.makeText(
                this,
                "로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}