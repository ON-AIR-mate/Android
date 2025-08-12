package umc.onairmate.ui.friend.chat

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.data.socket.SocketDispatcher
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.databinding.FragmentVideoChatBinding
import umc.onairmate.ui.chat_room.message.ChatRVAdapter
import umc.onairmate.ui.chat_room.message.VideoChatViewModel
import umc.onairmate.ui.friend.FriendViewModel
import umc.onairmate.ui.util.SharedPrefUtil
import kotlin.getValue

@AndroidEntryPoint
class FriendChatFragment: Fragment() {
    private val TAG = javaClass.simpleName
    private var _binding: FragmentVideoChatBinding? = null
    private val binding get() = _binding!!
    private var user : UserData = UserData()
    private var friend : FriendData = FriendData()
    private val viewModel: FriendChatViewModel by viewModels()

    lateinit var adapter : FriendChatRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoChatBinding.inflate(inflater, container, false)

        initData()
        setUpObserver()


        // 소켓 연결
        val socket = SocketManager.getSocketOrNull()
        if (socket?.connected() == true) {
            SocketDispatcher.registerHandler(socket, viewModel.getHandler())
        }


        adapter = FriendChatRVAdapter(user.userId)
        binding.rvVideoChat.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvVideoChat.adapter = adapter

        binding.btnSend.setOnClickListener {
            val text = binding.etInputChat.text.toString()
            viewModel.sendMessage(friend.userId, user.nickname, text)
            binding.etInputChat.setText("")
        }


        viewModel.joinDM(friend.userId)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initData(){
        friend = arguments?.getParcelable("friendData", FriendData::class.java)!!
        user = SharedPrefUtil.getData("user_info")?: UserData()
    }

    private fun setUpObserver() {
        viewModel.generalChat.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            val name = if (data.senderId == user.userId) user.nickname else friend.nickname
            val profile = if (data.senderId == user.userId) user.profileImage else friend.nickname
            val chat = ChatMessageData(
                messageId = 0,
                userId= data.senderId,
                nickname = name,
                profileImage = profile,
                content= data.content,
                messageType =  data.messageType,
                timestamp = data.createdAt)
            adapter.addGeneralChat(chat)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}