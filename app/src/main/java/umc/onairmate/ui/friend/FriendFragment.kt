package umc.onairmate.ui.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.databinding.FragmentFriendBinding
import kotlin.getValue

@AndroidEntryPoint
class FriendFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!

    private val tabList = arrayListOf("친구 목록", "받은 요청", "친구 찾기")
    private val viewModel: FriendViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = FriendVPAdapter(this)
        binding.vpFriend.adapter = adapter
        binding.vpFriend.setUserInputEnabled(false);
        TabLayoutMediator(binding.tbFriend, binding.vpFriend){
                tab, position ->
            tab.text = tabList[position]
        }.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}