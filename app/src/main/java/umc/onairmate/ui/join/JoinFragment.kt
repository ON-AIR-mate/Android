package umc.onairmate.ui.join

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import umc.onairmate.R
import umc.onairmate.databinding.FragmentJoinBinding

class JoinFragment : Fragment() {
    private var _binding: FragmentJoinBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoinBinding.inflate(inflater, container, false)

        setupCheckBoxLogic()
        setupArrowClickListeners()
        setupJoinButton()

        return binding.root
    }

    private fun setupCheckBoxLogic() = with(binding) {
        val allRequiredCheckBoxes = listOf(
            serviceCheckBox,
            privacyCheckBox,
            privacyProcessCheckBox
        )

        val allCheckBoxes = listOf(
            serviceCheckBox,
            privacyCheckBox,
            privacyProcessCheckBox,
            marketingCheckBox,
            eventCheckBox
        )

        // 전체 동의 클릭 시
        allAgreeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            allCheckBoxes.forEach { it.isChecked = isChecked }
            updateJoinButtonState()
        }

        // 개별 체크박스가 바뀔 때
        allCheckBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                // 전체 동의 상태 갱신
                allAgreeCheckBox.isChecked = allCheckBoxes.all { it.isChecked }
                updateJoinButtonState()
            }
        }
    }

    private fun updateJoinButtonState() = with(binding) {
        val requiredChecked = serviceCheckBox.isChecked &&
                privacyCheckBox.isChecked &&
                privacyProcessCheckBox.isChecked

        joinBtn.isEnabled = requiredChecked
        joinBtn.setBackgroundResource(
            if (requiredChecked) R.color.main else R.color.disable
        )
    }

    private fun setupJoinButton() = with(binding) {
        joinBtn.setOnClickListener {
            findNavController().navigate(R.id.fragment_join_profile)
        }
    }

    private fun setupArrowClickListeners() = with(binding) {
        arrowService.setOnClickListener {
            showTermsDialog("온에어메이트 서비스 이용약관", R.string.service_terms)
        }
        arrowPrivacy.setOnClickListener {
            showTermsDialog("개인정보 수집 및 이용 동의", R.string.privacy_terms)
        }
        arrowProcessing.setOnClickListener {
            showTermsDialog("개인정보 처리방침", R.string.privacy_process_terms)
        }
        arrowMarketing.setOnClickListener {
            showTermsDialog("마케팅 수신 동의", R.string.marketing_terms)
        }
        arrowEvent.setOnClickListener {
            showTermsDialog("이벤트/프로모션 참여 동의", R.string.event_terms)
        }
    }

    private fun showTermsDialog(title: String, messageResId: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(getString(messageResId))
            .setPositiveButton("확인", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}