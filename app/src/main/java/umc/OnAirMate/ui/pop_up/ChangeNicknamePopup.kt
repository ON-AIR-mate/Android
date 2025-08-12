package umc.onairmate.ui.pop_up

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import umc.onairmate.R
import umc.onairmate.data.repository.NicknameRepository
import umc.onairmate.databinding.PopupChangeNicknameBinding
import javax.inject.Inject

@AndroidEntryPoint
class ChangeNicknamePopup : BottomSheetDialogFragment() {

    private var _binding: PopupChangeNicknameBinding? = null
    private val binding get() = _binding!!

//    @Inject
//    lateinit var repository: NicknameRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PopupChangeNicknameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editNickname.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                val isValid = input.length in 3..10

                binding.checkNickname.setBackgroundResource(
                    if (isValid) R.drawable.bg_btn_main
                    else R.drawable.bg_btn_disabled
                )
                binding.checkNickname.isEnabled = isValid
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.checkNickname.setOnClickListener {
            val nickname = binding.editNickname.text.toString()

            if (nickname.length !in 3..10) {
                Toast.makeText(requireContext(), "닉네임은 3자 이상 10자 이하여야 합니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            viewLifecycleOwner.lifecycleScope.launch {
//                val isDuplicated = withContext(Dispatchers.IO) {
//                    repository.isNicknameDuplicated(nickname)
//                }
//
//                if (isDuplicated) {
//                    Toast.makeText(requireContext(), "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
//                    binding.editNickname.text.clear()
//                    binding.checkNickname.setBackgroundResource(R.drawable.bg_btn_disabled)
//                    binding.checkNickname.isEnabled = false
//                } else {
//                    Toast.makeText(requireContext(), "사용 가능한 닉네임입니다!", Toast.LENGTH_SHORT).show()
//                }
//            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}