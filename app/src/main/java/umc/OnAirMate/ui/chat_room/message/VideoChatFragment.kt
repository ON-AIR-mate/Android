package umc.onairmate.ui.chat_room.message

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.socket.SocketDispatcher
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.databinding.FragmentVideoChatBinding
import kotlin.getValue

@AndroidEntryPoint
class VideoChatFragment: Fragment() {
    private val TAG = javaClass.simpleName
    private var _binding: FragmentVideoChatBinding? = null
    private val binding get() = _binding!!
    private var userId : Int = 0
    private var roomId : Int = 0
    private var nickname : String = ""
    lateinit var adapter : ChatRVAdapter
    private val videoChatViewModel: VideoChatViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoChatBinding.inflate(inflater, container, false)

        initData()
        setUpObserver()


        adapter = ChatRVAdapter(userId)
        binding.rvVideoChat.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvVideoChat.adapter = adapter

        binding.btnSend.setOnClickListener {
            val text = binding.etInputChat.text.toString()
            val type = if (checkBookMark(text)) "system" else "general"
            videoChatViewModel.sendMessage(roomId, nickname, text, type)
            binding.etInputChat.setText("")
        }

        videoChatViewModel.joinRoom(roomId,nickname)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoChatViewModel.getChatHistory(roomId)
    }
    private fun checkBookMark(input: String): Boolean {
        val regex = Regex("^\\d{2}:\\d{2}:\\d{2}")
        val result = regex.containsMatchIn(input)
        Log.d("CheckBookMark", "input=[$input], result=$result")
        return result
    }
    override fun onResume() {
        super.onResume()

    }

    private fun initData(){
        val spf = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        userId = spf.getInt("userId", 0)
        nickname = spf.getString("nickname","nickname")?:"user"
        roomId = arguments?.getInt("roomId", 0)!!
        Log.d(TAG,"id : ${roomId}")

    }

    private fun setUpObserver() {
        videoChatViewModel.chatHistory.observe(viewLifecycleOwner) { list ->
            if (list == null) return@observe
            adapter.initChatHistory(list)
        }

        videoChatViewModel.chat.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            adapter.addChat(data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        videoChatViewModel.leaveRoom(roomId) // 방 나가기 (Socket)
        SocketManager.getSocketOrNull()?.let { socket ->
            if (socket.connected()) {
                SocketDispatcher.unregisterHandler(socket, videoChatViewModel.getHandler())
            }
        }
        _binding = null
    }
}