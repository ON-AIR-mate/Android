package umc.onairmate.ui.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.databinding.FragmentProfileBinding
import umc.onairmate.ui.TestViewModel
import kotlin.getValue

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private var num : Int = 10
    private val viewModel: TestViewModel by viewModels()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding =  FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tvSignUp.setOnClickListener {
            viewModel.signUp(num)
        }
        binding.tvLogin.setOnClickListener {
            viewModel.login(num++)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
