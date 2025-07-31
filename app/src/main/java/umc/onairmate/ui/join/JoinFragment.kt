package umc.onairmate.ui.join

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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

        return binding.root
    }

    private fun setupCheckBoxLogic() = with(binding) {
        // 전체 체크박스가 변경될 때 개별 체크박스들 모두 변경
        allAgreeCheckBox.setOnCheckedChangeListener { _, isChecked ->
            serviceCheckBox.isChecked = isChecked
            privacyCheckBox.isChecked = isChecked
            privacyProcessCheckBox.isChecked = isChecked
            marketingCheckBox.isChecked = isChecked
            eventCheckBox.isChecked = isChecked
        }

        // 개별 체크박스 리스트
        val individualCheckBoxes = listOf(
            serviceCheckBox,
            privacyCheckBox,
            privacyProcessCheckBox,
            marketingCheckBox,
            eventCheckBox
        )

        // 개별 체크박스들 상태에 따라 전체 체크박스 갱신
        individualCheckBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                allAgreeCheckBox.isChecked = individualCheckBoxes.all { it.isChecked }
            }
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