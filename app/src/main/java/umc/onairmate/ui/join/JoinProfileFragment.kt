package com.example.onairmate.ui.join

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import umc.onairmate.R
import umc.onairmate.databinding.FragmentJoinProfileBinding

class JoinProfileFragment : Fragment() {

    private var _binding: FragmentJoinProfileBinding? = null
    private val binding get() = _binding!!

    // 유효성 검사 상태
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

        initListeners()
    }

    private fun initListeners() {
        binding.tvNicknameStatus.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            checkNicknameDuplication(nickname)
        }

        binding.etId.addTextChangedListener {
            checkIdDuplication(it.toString())
        }

        binding.etPassword.addTextChangedListener(passwordWatcher)

        binding.btnComplete.setOnClickListener {
            if (isNicknameValid && isIdValid && isPasswordValid) {
                // 저장 로직
                Toast.makeText(requireContext(), "회원가입 완료", Toast.LENGTH_SHORT).show()
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
        } else {
            isNicknameValid = false
            binding.tvNicknameStatus.text = "중복확인"
            binding.tvNicknameStatus.setTextColor(resources.getColor(R.color.main, null))
            binding.tvNicknameGuide.text = "사용할 수 없는 닉네임입니다."
            binding.tvNicknameGuide.setTextColor(resources.getColor(R.color.main, null))
        }
        updateCompleteButtonState()
    }

    private fun checkIdDuplication(id: String) {
        // TODO: DB 중복 확인 로직 삽입
        if (id == "onairmate1234") {
            isIdValid = false
            binding.tvIdGuide.text = "사용할 수 없는 아이디입니다."
            binding.tvIdGuide.setTextColor(resources.getColor(R.color.main, null))
            binding.tvIdGuide.visibility = View.VISIBLE
        } else {
            isIdValid = true
            binding.tvIdGuide.text = "사용 가능한 아이디입니다."
            binding.tvIdGuide.setTextColor(resources.getColor(R.color.canuse, null))
            binding.tvIdGuide.visibility = View.VISIBLE
        }
        updateCompleteButtonState()
    }

    private val passwordWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            val password = s.toString()
            isPasswordValid = password.matches(Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*()_+=-]).{8,16}\$"))
            if (isPasswordValid) {
                binding.tvPasswordGuide.visibility = View.GONE
            } else {
                binding.tvPasswordGuide.text =
                    "8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해주세요."
                binding.tvPasswordGuide.setTextColor(resources.getColor(R.color.main, null))
                binding.tvPasswordGuide.visibility = View.VISIBLE
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

        val canEnalbe = isAllFilled && isAllValid
        binding.btnComplete.isEnabled = isAllValid
        binding.btnComplete.setBackgroundResource(
            if (canEnalbe) R.drawable.bg_btn_main else R.drawable.bg_btn_disabled
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}