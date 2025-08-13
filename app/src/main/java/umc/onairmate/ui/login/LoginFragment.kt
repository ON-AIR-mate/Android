package umc.onairmate.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.ActivityLoginBinding
import umc.onairmate.databinding.FragmentLoginBinding
import umc.onairmate.ui.MainActivity
import umc.onairmate.ui.TestViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val loginViewModel: LoginViewModel by viewModels({ requireActivity() })
    private val testViewModel: TestViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // 버튼 활성화 예시
        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val idOk = binding.etId.text.isNotBlank()
                val pwOk = binding.etPassword.text.isNotBlank()
                binding.btnLogin.isEnabled = idOk && pwOk
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }
        binding.etId.addTextChangedListener(watcher)
        binding.etPassword.addTextChangedListener(watcher)

        // 로그인 클릭
        binding.btnLogin.setOnClickListener {
            val id = binding.etId.text.toString()
            val pw = binding.etPassword.text.toString()
            if (id.isBlank() || pw.isBlank()) return@setOnClickListener
            // loginViewModel.login(id, pw)
        }

        // 로그인 성공/실패 관찰 (활용은 네가 쓰던 로직 재사용)
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            if (result.isSuccess) {
                Toast.makeText(requireContext(), "로그인 성공!", Toast.LENGTH_SHORT).show()
                // Main으로 이동 등
                startActivity(Intent(requireContext(), MainActivity::class.java))
                // requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 진입
        binding.newprofile.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_joinFragment)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
