package umc.onairmate.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import umc.onairmate.databinding.FragmentProfileBinding
import umc.onairmate.ui.pop_up.ChangeNicknamePopup
import umc.onairmate.ui.pop_up.ChangeNicknameViewModel

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // ChangeNicknameViewModel 선언
    private val changeNicknameViewModel: ChangeNicknameViewModel by viewModels()

    private var nickname = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val spf = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        nickname = spf.getString("nickname","user")!!

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvNicknameValue.text = nickname

        // 닉네임 영역 클릭 시 팝업 띄우기
        binding.layoutNickname.setOnClickListener {
            val popup = ChangeNicknamePopup()

            // 팝업에 중복 검사 콜백 연결
            popup.onCheckNickname = { newNickname, callback ->
                // 뷰모델의 checkNickname 함수 사용
                changeNicknameViewModel.checkNickname(newNickname) { isDuplicated ->
                    // 결과 콜백 호출
                    callback(isDuplicated)
                }
            }

            popup.show(childFragmentManager, "ChangeNicknamePopup")
        }

        // 기존에 있던 다른 클릭 리스너들 유지
        binding.btnChangeProfile.setOnClickListener {
            Toast.makeText(requireContext(), "프로필 사진 변경 클릭", Toast.LENGTH_SHORT).show()
        }

        binding.ivTooltip.setOnClickListener {
            Toast.makeText(requireContext(), "추천 및 제재에 따라 인기도가 조정됩니다.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}