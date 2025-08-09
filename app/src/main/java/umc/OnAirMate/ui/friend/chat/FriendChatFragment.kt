package umc.onairmate.ui.friend.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.socket.SocketDispatcher
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.databinding.FragmentVideoChatBinding
import umc.onairmate.ui.chat_room.message.ChatRVAdapter
import umc.onairmate.ui.chat_room.message.VideoChatViewModel
import umc.onairmate.ui.friend.FriendViewModel
import kotlin.getValue

@AndroidEntryPoint
class FriendChatFragment: Fragment() {
    private val TAG = javaClass.simpleName
    private var _binding: FragmentVideoChatBinding? = null
    private val binding get() = _binding!!
    private var userId : Int = 0
    private var nickname : String = ""
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


        adapter = FriendChatRVAdapter(userId)
        binding.rvVideoChat.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvVideoChat.adapter = adapter

        binding.btnSend.setOnClickListener {
            val text = binding.etInputChat.text.toString()
            viewModel.sendMessage(37, nickname, text)
            binding.etInputChat.setText("")
        }


        viewModel.joinDM(37)
        return binding.root
    }


    override fun onResume() {
        super.onResume()
    }

    private fun initData(){
        val spf = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        userId = spf.getInt("userId", 0)
        nickname = spf.getString("nickname","nickname")?:"user"


    }

    private fun setUpObserver() {
        viewModel.generalChat.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            val chat = ChatMessageData(0,0,userId,data.sender,"",data.message,"GENERAL","")
            adapter.addGeneralChat(chat)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}