package umc.onairmate.ui.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
<<<<<<< HEAD:app/src/main/java/umc/OnAirMate/ui/friend/FriendFragment.kt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FriendFragment : Fragment() {
=======
import umc.onairmate.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
>>>>>>> origin/main:app/src/main/java/umc/onairmate/ui/profile/ProfileFragment.kt

    private var _binding: FragmentProfileBinding? = null

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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
