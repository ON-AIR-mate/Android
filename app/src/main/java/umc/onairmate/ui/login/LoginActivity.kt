package umc.onairmate.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.ui.join.JoinProfileFragment
import umc.onairmate.databinding.ActivityLoginBinding
import umc.onairmate.ui.MainActivity
import umc.onairmate.ui.TestViewModel
import kotlin.getValue

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: TestViewModel by viewModels()


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

            viewModel.signUp(userId,userPw)

        }

        // 회원가입 화면 이동
        binding.newprofile.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(umc.onairmate.R.id.fragment_container, JoinProfileFragment())
                .addToBackStack(null)
                .commit()
        }

        viewModel.isSuccess.observe(this){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        //아이디 비밀번호 입력 시 버튼 활성화 코드
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



}