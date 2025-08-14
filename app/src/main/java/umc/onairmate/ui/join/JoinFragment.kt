package umc.onairmate.ui.join

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.request.TestRequest
import umc.onairmate.data.model.request.TestRequest.Agreement
import umc.onairmate.databinding.FragmentJoinBinding
import umc.onairmate.ui.join.model.Agreements

@AndroidEntryPoint
class JoinFragment : Fragment() {
    private val TAG = javaClass.simpleName

    private var _binding: FragmentJoinBinding? = null
    private val binding get() = _binding!!

    private var checkBoxes = mutableListOf<CheckBox>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJoinBinding.inflate(inflater, container, false)


        initCheckBox()
        setClickListener()
        setupArrowClickListeners()


        return binding.root
    }

    private fun setClickListener() {
        binding.btnClose.setOnClickListener {
            findNavController().popBackStack(R.id.loginFragment, false)
        }

        binding.btnJoin.setOnClickListener {
            val agree = Agreement(
                serviceTerms = checkBoxes[0].isChecked,
                privacyCollection = checkBoxes[1].isChecked,
                privacyPolicy = checkBoxes[2].isChecked,
                marketingConsent = checkBoxes[3].isChecked,
                eventPromotion = checkBoxes[4].isChecked
            )
            val bundle = Bundle().apply {
                putParcelable("agreements",agree)
            }
            Log.d(TAG,"agreements ${agree}")

            findNavController().navigate(R.id.action_joinFragment_to_joinProfileFragment, bundle)
        }
    }


    private fun initCheckBox(){
        checkBoxes.add(binding.cbService)
        checkBoxes.add(binding.cbPrivacy)
        checkBoxes.add(binding.cbPrivacyProcess)
        checkBoxes.add(binding.cbMarketing)
        checkBoxes.add(binding.cbEvent)
        setCheckBox()
    }

    private fun checkBtnEnabled(){
        val necessary = checkBoxes.slice(0..2)
        binding.btnJoin.isEnabled = necessary.all { it.isChecked }
    }

    private fun setCheckBox(){
        for(box in checkBoxes){
            box.setOnClickListener {
                checkAll()
                checkBtnEnabled()
            }
        }
        binding.cbAllAgree.setOnCheckedChangeListener { _, isChecked ->
            for(box in checkBoxes)
                box.isChecked = isChecked
            checkBtnEnabled()
        }
    }

    private fun checkAll(){
        val isAll =  checkBoxes.all { it.isChecked }
        binding.cbAllAgree.isChecked = isAll
    }


    private fun setupArrowClickListeners() = with(binding) {
        ivArrowService.setOnClickListener {
            showTermsDialog("[필수] 온에어메이트 서비스 이용약관", R.string.service_terms)
        }
        ivArrowPrivacy.setOnClickListener {
            showTermsDialog("[필수] 개인정보 수집 및 이용 동의", R.string.privacy_terms)
        }
        ivArrowProcessing.setOnClickListener {
            showTermsDialog("[필수] 개인정보 처리방침", R.string.privacy_process_terms)
        }
        ivArrowMarketing.setOnClickListener {
            showTermsDialog("[선택] 마케팅 수신 동의", R.string.marketing_terms)
        }
        ivArrowEvent.setOnClickListener {
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

    private fun backToLogin() = with(binding) {
        btnClose.setOnClickListener {
            findNavController().navigate(R.id.action_joinDetailFragment_to_joinFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
