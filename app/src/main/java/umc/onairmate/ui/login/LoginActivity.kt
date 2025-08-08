package umc.onairmate.ui.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.ActivityLoginBinding
import umc.onairmate.ui.join.JoinFragment

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 결과 관찰
        loginViewModel.loginResult.observe(this) { result ->
            if (result.isSuccess) {
                val user = result.getOrNull()
                Toast.makeText(
                    this,
                    "로그인 성공! ${user?.user?.nickname}님 환영합니다.",
                    Toast.LENGTH_SHORT
                ).show()
                // TODO: 토큰 저장, 화면 전환 처리
            } else {
                Toast.makeText(
                    this,
                    "로그인 실패: ${result.exceptionOrNull()?.message ?: "아이디 또는 비밀번호를 확인하세요."}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // 아이디/비밀번호 입력 시 로그인 버튼 활성화 처리
        val textWatcher = object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val idInput = binding.etId.text.toString()
                val pwInput = binding.etPassword.text.toString()
                val isEnabled = idInput.isNotBlank() && pwInput.isNotBlank()

                binding.btnLogin.isEnabled = isEnabled
                binding.btnLogin.backgroundTintList = android.content.res.ColorStateList.valueOf(
                    androidx.core.content.ContextCompat.getColor(
                        this@LoginActivity,
                        if (isEnabled) R.color.main else R.color.disable
                    )
                )
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        binding.etId.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)

        // 로그인 버튼 클릭 이벤트
        binding.btnLogin.setOnClickListener {
            val userId = binding.etId.text.toString()
            val userPw = binding.etPassword.text.toString()

            if (userId.isBlank() || userPw.isBlank()) {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            loginViewModel.login(userId, userPw) // 로그인 시도
        }

        // 회원가입 버튼 클릭 시 Fragment로 이동 처리
        binding.newprofile.setOnClickListener {
            // 로그인 UI 숨기기
            binding.loginContent.visibility = View.GONE
            // fragment_container 보이기
            binding.fragmentContainer.visibility = View.VISIBLE

            // Fragment 띄우기
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, JoinFragment())
                .addToBackStack(null)  // 뒤로가기 시 로그인 화면 복귀 가능
                .commit()
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
            // 로그인 UI 다시 보이기
            binding.loginContent.visibility = View.VISIBLE
            binding.fragmentContainer.visibility = View.GONE
        } else {
            super.onBackPressed()
        }
    }
}