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
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.FragmentLoginBinding
import umc.onairmate.ui.MainActivity
import umc.onairmate.ui.TestViewModel

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    //private val loginViewModel: LoginViewModel by viewModels({ requireActivity() })
    private val  loginViewModel: TestViewModel by viewModels({ requireActivity() })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setTextWatcher()
        setClickListener()
        setObserver()

    }

    private fun setTextWatcher(){
        // 버튼 활성화
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
    }

    private fun setClickListener(){
        // 회원가입 진입
        binding.newprofile.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_joinFragment)
        }

        // 로그인 클릭
        binding.btnLogin.setOnClickListener {
            val id = binding.etId.text.toString()
            val pw = binding.etPassword.text.toString()
            val autoLogin : Boolean = binding.cbKeepLogin.isChecked
            if (id.isBlank() || pw.isBlank()) return@setOnClickListener
            loginViewModel.login(id, pw, autoLogin)
        }
    }

    private fun setObserver(){
        loginViewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                // Main으로 이동 등
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            } else {
                Toast.makeText(requireContext(), "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
