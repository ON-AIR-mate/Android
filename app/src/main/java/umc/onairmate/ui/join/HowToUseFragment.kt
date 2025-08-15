package umc.onairmate.ui.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.FragmentHowToUseBinding
import umc.onairmate.ui.login.LoginActivity

@AndroidEntryPoint
class HowToUseFragment : Fragment() {

    private var _binding: FragmentHowToUseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHowToUseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnClose.setOnClickListener {
            findNavController().popBackStack(R.id.loginFragment, false)
        }

        binding.btnLogin.setOnClickListener {
            findNavController().popBackStack(R.id.loginFragment, false)
        }


        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack(R.id.loginFragment, false)
                    // Activity에 위임해 로그인 UI를 다시 보이게(현재 onBackPressed가 그 로직 보유)
                    (requireActivity() as LoginActivity).onBackPressed()
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}