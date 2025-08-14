package umc.onairmate.ui.profile


import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.FragmentProfileBinding


@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var nickname = ""
    private val userViewModel: UserViewModel by viewModels()
    
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

        binding.btnChangeProfile.setOnClickListener {
            Toast.makeText(requireContext(), "프로필 사진 변경 클릭", Toast.LENGTH_SHORT).show()
        }

        binding.ivTooltip.setOnClickListener {
            Toast.makeText(requireContext(), "추천 및 제재에 따라 인기도가 조정됩니다.", Toast.LENGTH_LONG).show()
        }

        binding.layoutMyRooms.setOnClickListener {
            // 참여한 방 이동
        }

        binding.tvNicknameValue.text = nickname

        // 다른 버튼들에 대한 clickListener도 동일하게 설정


        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            userViewModel.deleteState.collect { s ->
                when (s) {
                    UiState.Loading -> showLoading()
                    is UiState.Success -> { hideLoading(); toast(s.msg); refreshListAfterDelete() }
                    is UiState.Error -> { hideLoading(); toast(s.msg) }
                    else -> Unit
                }
            }
        }

        override fun onDeleteClick(roomId: Long) {
            AlertDialog.Builder(requireContext())
                .setMessage("참여한 방 기록을 삭제할까요?")
                .setPositiveButton("삭제") { _, _ -> userViewModel.deleteParticipated(roomId) }
                .setNegativeButton("취소", null)
                .show()
        }

        private fun refreshListAfterDelete() {
            // 서버/로컬 리스트 갱신 로직 (재호출 or 로컬 제거) 프로젝트 방식대로
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //도움말 클릭시 글귀 표시
    private fun showTooltip(anchorView: View, message: String) {
        val inflater = LayoutInflater.from(anchorView.context)
        val popupView = inflater.inflate(R.layout.popup_tooltip, null)

        // 텍스트 설정
        val tooltipText = popupView.findViewById<TextView>(R.id.tooltip_text)
        tooltipText.text = message

        // PopupWindow 생성
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        // 위치 계산
        val location = IntArray(2)
        anchorView.getLocationOnScreen(location)

        val anchorX = location[0]
        val anchorY = location[1]

        // anchorView 위에 말풍선 위치 조정
        popupWindow.showAtLocation(
            anchorView, Gravity.NO_GRAVITY,
            anchorX - popupView.measuredWidth / 2 + anchorView.width / 2,
            anchorY - anchorView.height - 20  // 말풍선 높이 조절
        )

        binding.ivTooltip.setOnClickListener {
            showTooltip(it, "추천 및 제재에 따라 인기도가 조정됩니다.")
        }
    }
}