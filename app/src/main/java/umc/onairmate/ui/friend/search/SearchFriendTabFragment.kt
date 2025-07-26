package umc.onairmate.ui.friend.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.databinding.FragmentSearchFriendTabBinding

@AndroidEntryPoint
class SearchFriendTabFragment: Fragment() {
    private val TAG = this.javaClass.simpleName
    private var _binding: FragmentSearchFriendTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFriendTabBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}