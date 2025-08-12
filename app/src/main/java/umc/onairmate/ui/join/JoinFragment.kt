package umc.onairmate.ui.join

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.FragmentJoinBinding

@AndroidEntryPoint
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
        setupCompleteButton()

        return binding.root
    }

    private fun setupCheckBoxLogic() = with(binding) {
        allAgreeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            serviceCheckBox.isChecked = isChecked
            privacyCheckBox.isChecked = isChecked
            privacyProcessCheckBox.isChecked = isChecked
            marketingCheckBox.isChecked = isChecked
            eventCheckBox.isChecked = isChecked
        }

        val individualCheckBoxes = listOf(
            serviceCheckBox,
            privacyCheckBox,
            privacyProcessCheckBox,
            marketingCheckBox,
            eventCheckBox
        )

        individualCheckBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                allAgreeCheckBox.isChecked = individualCheckBoxes.all { it.isChecked }
                updateCompleteButtonState()
            }
        }
    }

    private fun updateCompleteButtonState() = with(binding) {
        val allRequiredChecked = serviceCheckBox.isChecked &&
                privacyCheckBox.isChecked &&
                privacyProcessCheckBox.isChecked

        if (allRequiredChecked) {
            joinBtn.isEnabled = true
            joinBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.main))
        } else {
            joinBtn.isEnabled = false
            joinBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.disable))
        }
    }

    private fun setupCompleteButton() = with(binding) {
        joinBtn.isEnabled = false
        joinBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.disable))

        joinBtn.setOnClickListener {
            // Navigation Component 방식으로 이동
            findNavController().navigate(R.id.action_joinFragment_to_joinProfileFragment)
        }
    }

    private fun showServiceDetail() = with(binding) {
        arrowService.setOnClickListener {
            findNavController().navigate(R.id.action_joinDetailFragment_to_joinFragment)
        }
    }

    private fun backToLogin() = with(binding) {
        btnClose.setOnClickListener {
            findNavController().navigate(R.id.action_joinDetailFragment_to_joinFragment)
        }
    }


    private fun setupArrowClickListeners() = with(binding) {
        arrowService.setOnClickListener {
            showTermsDialog("[필수] 온에어메이트 서비스 이용약관", R.string.service_terms)
        }
        arrowPrivacy.setOnClickListener {
            showTermsDialog("[필수] 개인정보 수집 및 이용 동의", R.string.privacy_terms)
        }
        arrowProcessing.setOnClickListener {
            showTermsDialog("[필수] 개인정보 처리방침", R.string.privacy_process_terms)
        }
        arrowMarketing.setOnClickListener {
            showTermsDialog("[선택] 마케팅 수신 동의", R.string.marketing_terms)
        }
        arrowEvent.setOnClickListener {
            showTermsDialog("[선택] 이벤트/프로모션 참여 동의", R.string.event_terms)
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
