package umc.onairmate.ui.profile

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import umc.onairmate.R
import umc.onairmate.databinding.FragmentPersonalInfoBinding

class PersonalInfoFragment : Fragment() {

    private var _binding: FragmentPersonalInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogout.setOnClickListener {
            Toast.makeText(requireContext(), "로그아웃 클릭", Toast.LENGTH_SHORT).show()
        }

//        binding.ivTooltip.setOnClickListener {
//            showTooltip(it, "추천 및 제재에 따라 인기도가 조정됩니다.")
//        }
//
//        binding.btnEditNickname.setOnClickListener {
//            Toast.makeText(requireContext(), "닉네임 수정 클릭", Toast.LENGTH_SHORT).show()
//        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showTooltip(anchorView: View, message: String) {
        val inflater = LayoutInflater.from(anchorView.context)
        val popupView = inflater.inflate(R.layout.popup_tooltip, null)

        val tooltipText = popupView.findViewById<TextView>(R.id.tooltip_text)
        tooltipText.text = message

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        ).apply {
            isOutsideTouchable = true
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        anchorView.post {
            popupWindow.showAsDropDown(anchorView, -anchorView.width, -anchorView.height * 2)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
