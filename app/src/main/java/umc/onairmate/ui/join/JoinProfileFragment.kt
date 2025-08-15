package umc.onairmate.ui.join

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.request.TestRequest.*
import umc.onairmate.databinding.FragmentJoinProfileBinding
import umc.onairmate.ui.ImageViewModel
import umc.onairmate.ui.login.LoginViewModel
import umc.onairmate.ui.util.ImagePickerDelegate
import umc.onairmate.ui.util.NetworkImageLoader
import kotlin.getValue

@AndroidEntryPoint
class JoinProfileFragment : Fragment() {

    private var _binding: FragmentJoinProfileBinding? = null
    private val binding get() = _binding!!

    private var isNicknameValid = false
    private var isIdValid = true
    private var isPasswordValid = false

    private var nickname : String = ""
    private var id : String = ""
    private var password : String = ""
    private var profile : String = "default"

    private val imageViewModel: ImageViewModel by viewModels()
    private lateinit var picker: ImagePickerDelegate

    private val loginViewModel: LoginViewModel by viewModels()

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
        setUpObserver()
        setImagePicker()
    }

    private fun initUI() {
        // 처음 진입 시 안내문구 숨김
        binding.tvNicknameGuide.visibility = View.INVISIBLE
        binding.tvIdGuide.visibility = View.INVISIBLE

    }

    private fun initListeners() {
        // 닉네임 중복확인 버튼
        binding.tvNicknameStatus.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            loginViewModel.checkNickname(nickname)
        }

        // 비밀번호 유효성 체크
        binding.etPassword.addTextChangedListener(passwordWatcher)

        // 완료 버튼 클릭 시 다음 프래그먼트로 이동
        binding.btnJoin.setOnClickListener {
            val agreement = arguments?.getParcelable<Agreement>("agreement")?: Agreement()
            loginViewModel.signUp(
                id = id,
                pw = password,
                nickname = nickname,
                profile =  profile,
                agreements = agreement
            )
        }

        binding.btnProfileSelect.setOnClickListener {
            picker.launch()
        }

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack(R.id.loginFragment, false)
        }
    }

    // 이미지 선택창 출력
    private fun setImagePicker(){
        picker = ImagePickerDelegate(this) { uri ->
            if (uri != null) {
                imageViewModel.uploadUri(uri)
            }
        }
        picker.register()
    }

    private fun setUpObserver(){
        loginViewModel.available.observe(viewLifecycleOwner){available ->
            if (available == null) return@observe
            if (available){
                binding.tvNicknameStatus.text = "사용가능"
                binding.tvNicknameStatus.setTextColor(resources.getColor(R.color.canuse, null))
                binding.tvNicknameGuide.text = "사용 가능한 닉네임입니다."
                binding.tvNicknameGuide.setTextColor(resources.getColor(R.color.canuse, null))
            }
            else {
                binding.tvNicknameStatus.text = "중복확인"
                binding.tvNicknameStatus.setTextColor(resources.getColor(R.color.main, null))
                binding.tvNicknameGuide.text = "사용할 수 없는 닉네임입니다."
                binding.tvNicknameGuide.setTextColor(resources.getColor(R.color.main, null))
            }
            isNicknameValid = available
            binding.tvNicknameGuide.visibility = View.VISIBLE
            updateCompleteButtonState()
        }

        loginViewModel.isSuccess.observe(viewLifecycleOwner){isSuccess ->
            if (isSuccess == null) return@observe
            if (isSuccess){
                findNavController().navigate(R.id.action_joinProfileFragment_to_howToUseFragment)
            }
            else{
                Toast.makeText(requireContext(), "입력값을 다시 확인해주세요.", Toast.LENGTH_SHORT).show()
                binding.tvIdGuide.visibility = View.INVISIBLE
            //binding.btnJoin.isEnabled = isSuccess
            }
            loginViewModel.clearSuccess()
        }
        imageViewModel.imageUrl.observe(viewLifecycleOwner){ url ->
            if (url == null) return@observe
            profile = url
            NetworkImageLoader.profileLoad(binding.ivProfile, profile)
        }

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
        nickname = binding.etNickname.text.toString()
        id = binding.etId.text.toString()
        password = binding.etPassword.text.toString()

        val isAllFilled = nickname.isNotBlank() && id.isNotBlank() && password.isNotBlank()
        val isAllValid = isNicknameValid && isIdValid && isPasswordValid

        val canEnable = isAllFilled && isAllValid

        binding.btnJoin.isEnabled = canEnable
    }

    private fun checkIdDuplication(id: String) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}