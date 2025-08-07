package umc.onairmate.ui.join

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.FragmentJoinProfileBinding

@AndroidEntryPoint
class JoinProfileFragment : Fragment() {

    private var _binding: FragmentJoinProfileBinding? = null
    private val binding get() = _binding!!

    private var isNicknameValid = false
    private var isIdValid = false
    private var isPasswordValid = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoinProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        initListeners()
    }

    private fun initUI() {
        // 처음 진입 시 안내문구 숨김
        binding.tvNicknameGuide.visibility = View.GONE
        binding.tvIdGuide.visibility = View.GONE

        // 비밀번호 안내는 기본 노출
        binding.tvPasswordGuide.text =
            "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해주세요."
        binding.tvPasswordGuide.setTextColor(resources.getColor(R.color.main, null))
        binding.tvPasswordGuide.visibility = View.VISIBLE
    }

    private fun initListeners() {
        // 닉네임 중복확인 버튼
        binding.tvNicknameStatus.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            if (nickname.isBlank()) {
                binding.tvNicknameGuide.visibility = View.GONE
                return@setOnClickListener
            }
            checkNicknameDuplication(nickname)
        }

        // 아이디 입력 시 실시간 중복 확인
        binding.etId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val id = s.toString()
                if (id.isBlank()) {
                    binding.tvIdGuide.visibility = View.GONE
                    isIdValid = false
                } else {
                    checkIdDuplication(id)
                }
                updateCompleteButtonState()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 비밀번호 유효성 체크
        binding.etPassword.addTextChangedListener(passwordWatcher)

        // 완료 버튼 클릭 시 다음 프래그먼트로 이동
        binding.btnComplete.setOnClickListener {
            if (isNicknameValid && isIdValid && isPasswordValid) {
                parentFragmentManager.commit {
                    replace(R.id.fragment_container, HowToUseFragment())
                    addToBackStack(null)
                }
            } else {
                Toast.makeText(requireContext(), "입력값을 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkNicknameDuplication(nickname: String) {
        // TODO: DB 중복 확인 로직 삽입
        if (nickname == "이현서") {
            isNicknameValid = true
            binding.tvNicknameStatus.text = "사용가능"
            binding.tvNicknameStatus.setTextColor(resources.getColor(R.color.canuse, null))
            binding.tvNicknameGuide.text = "사용 가능한 닉네임입니다."
            binding.tvNicknameGuide.setTextColor(resources.getColor(R.color.canuse, null))
            binding.tvNicknameGuide.visibility = View.VISIBLE
        } else {
            isNicknameValid = false
            binding.tvNicknameStatus.text = "중복확인"
            binding.tvNicknameStatus.setTextColor(resources.getColor(R.color.main, null))
            binding.tvNicknameGuide.text = "사용할 수 없는 닉네임입니다."
            binding.tvNicknameGuide.setTextColor(resources.getColor(R.color.main, null))
            binding.tvNicknameGuide.visibility = View.VISIBLE
        }
        updateCompleteButtonState()
    }

    private fun checkIdDuplication(id: String) {
        // TODO: DB 중복 확인 로직 삽입
        if (id == "onairmate1234") {
            isIdValid = false
            binding.tvIdGuide.text = "사용할 수 없는 아이디입니다."
            binding.tvIdGuide.setTextColor(resources.getColor(R.color.main, null))
        } else {
            isIdValid = true
            binding.tvIdGuide.text = "사용 가능한 아이디입니다."
            binding.tvIdGuide.setTextColor(resources.getColor(R.color.canuse, null))
        }
        binding.tvIdGuide.visibility = View.VISIBLE
        updateCompleteButtonState()
    }

    private val passwordWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val password = s.toString()
            isPasswordValid = password.matches(Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+=-]).{8,16}\$"))
            if (isPasswordValid) {
                binding.tvPasswordGuide.setTextColor(resources.getColor(R.color.text4, null))
            } else {
                binding.tvPasswordGuide.setTextColor(resources.getColor(R.color.main, null))
            }
            updateCompleteButtonState()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun updateCompleteButtonState() {
        val nickname = binding.etNickname.text.toString()
        val id = binding.etId.text.toString()
        val password = binding.etPassword.text.toString()

        val isAllFilled = nickname.isNotBlank() && id.isNotBlank() && password.isNotBlank()
        val isAllValid = isNicknameValid && isIdValid && isPasswordValid

        val canEnable = isAllFilled && isAllValid

        binding.btnComplete.isEnabled = canEnable
        binding.btnComplete.setBackgroundResource(
            if (canEnable) R.drawable.bg_btn_main else R.drawable.bg_btn_disabled
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}