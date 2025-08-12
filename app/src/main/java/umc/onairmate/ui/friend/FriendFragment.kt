package umc.onairmate.ui.friend

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.databinding.FragmentFriendBinding
import umc.onairmate.ui.friend.chat.FriendChatActivity
import kotlin.getValue

@AndroidEntryPoint
class FriendFragment : Fragment() {
    private val TAG = this.javaClass.simpleName
    private var _binding: FragmentFriendBinding? = null
    private val binding get() = _binding!!

    private val tabList = arrayListOf("친구 목록", "받은 요청", "친구 찾기")
    private val viewModel: FriendViewModel by viewModels()

    // 결과 콜백
    private val  friendChatActivityLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val goSearch = result.data?.getBooleanExtra("go_search_video", false) == true
                if (goSearch) {
                    findNavController().navigate(R.id.action_friend_to_search_video)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 자식 탭(ChildTabFragment)에서 올라오는 "열어줘" 신호 수신
        childFragmentManager.setFragmentResultListener(
            "open_friend_chat_activity",
            this
        ) { _, bundle ->
            val item = bundle.getParcelable<FriendData>("friendData") ?: return@setFragmentResultListener
            val intent = Intent(requireContext(), FriendChatActivity::class.java).apply {
                putExtra("friendData", item)
            }
            friendChatActivityLauncher.launch(intent)
        }
    }

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
        initClickListener()
        return root
    }
    private fun initClickListener(){
        binding.ivYoutubeSearch.setOnClickListener {
            findNavController().navigate(R.id.action_friend_to_search_video)
        }
        var pos = 20
        binding.ivNotification.setOnClickListener {
            viewModel.acceptFriend(pos++,"ACCEPT")
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}