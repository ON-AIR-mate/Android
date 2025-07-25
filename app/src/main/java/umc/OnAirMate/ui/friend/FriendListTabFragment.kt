package umc.OnAirMate.ui.friend

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import umc.OnAirMate.databinding.FragmentFriendListTabBinding

@AndroidEntryPoint
class FriendListTabFragment: Fragment() {
    private val TAG = this.javaClass.simpleName
    private var _binding: FragmentFriendListTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendListTabBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}