package umc.OnAirMate.ui.login

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.databinding.ActivityLoginBinding
import com.example.onairmate.ui.join.JoinProfileFragment
import kotlinx.coroutines.launch
import umc.onairmate.data.model.entity.LoginData
import umc.onairmate.data.model.response.LoginResponse
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.ui.login.LoginViewModel
import umc.onairmate.ui.login.RetrofitClient

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //############ 로그인 결과 관찰
        loginViewModel.loginResult.observe(this) { result ->
            when {
                result.isSuccess -> {
                    val user = result.getOrNull()
                    Toast.makeText(
                        this,
                        "로그인 성공! ${user?.user?.nickname}님 환영합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // TODO: 토큰 저장, 화면 전환 처리
                }

                else -> {
                    Toast.makeText(
                        this,
                        "로그인 실패: ${result.exceptionOrNull()?.message ?: "아이디 또는 비밀번호를 확인하세요."}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // 로그인 버튼 클릭 이벤트
        binding.btnLogin.setOnClickListener {
            val userId = binding.etId.text.toString()
            val userPw = binding.etPassword.text.toString()

            if (userId.isBlank() || userPw.isBlank()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginViewModel.login(userId, userPw) //############
        }

        // 회원가입 화면 이동
        binding.newprofile.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(umc.onairmate.R.id.fragment_container, JoinProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        // 아이디 비밀번호 입력 시 버튼 활성화 코드
        val textWatcher = object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val idInput = binding.etId.text.toString()
                val pwInput = binding.etPassword.text.toString()
                val isEnabled = idInput.isNotBlank() && pwInput.isNotBlank()

                binding.btnLogin.isEnabled = isEnabled
                binding.btnLogin.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    androidx.core.content.ContextCompat.getColor(
                        this@LoginActivity,
                        if (isEnabled) umc.onairmate.R.color.main else umc.onairmate.R.color.disable
                    )
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.etId.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)
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

    /*
    private fun login(username: String, password: String) {

        // 임시 테스트용 로컬 로그인 코드 주석 처리
        val testUser = TestRequest(
            username = "testuser",
            password = "1234",
            nickname = "테스트닉네임",
            profileImage = "https://example.com/profile.jpg",
            agreements = TestRequest.Agreement()
        )

        if (username == testUser.username && password == testUser.password) {
            Toast.makeText(
                this,
                "로그인 성공! ${testUser.nickname}님 환영합니다.",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                "로그인 실패: 아이디 또는 비밀번호가 일치하지 않습니다.",
                Toast.LENGTH_SHORT
            ).show()
        }
        */

        // 실제 Retrofit API 호출로 로그인 처리
        /*
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.login(LoginRequest(username, password))
                if (response.isSuccessful && response.body()?.success == true) {
                    val loginData = response.body()!!.data!!
                    // TODO: 로그인 성공 후 토큰 저장 등 필요한 작업 진행
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

        lifecycleScope.launch {
            try {
                val request = LoginData(username, password)
                val response: RawDefaultResponse<LoginResponse> = RetrofitClient.instance.login(request)
                if (response.success /* 또는 response.isSuccess 등 */ && response.data != null) {
                    val loginData = response.data
                    Toast.makeText(
                        this@LoginActivity,
                        "로그인 성공! ${loginData.user.nickname}님 환영합니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    // TODO: 토큰 저장, 화면 전환 처리
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
}