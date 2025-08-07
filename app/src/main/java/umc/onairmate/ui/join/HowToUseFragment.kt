package umc.onairmate.ui.join

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.FragmentHowToUseBinding

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

        binding.ivClose.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

//        binding.btnLogin.setOnClickListener {
//            findNavController().navigate(R.id.fragment_container)
//        }
        //이부분 수정. 스크롤뷰에서는 네비게이션 컨트롤 사용 불가.
        binding.btnLogin.setOnClickListener {
            binding.root.findNavController().navigate(R.id.fragment_container)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}