package umc.onairmate.ui.chat_room.message

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    private val viewModel: VideoChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoChatBinding.inflate(inflater, container, false)

        initData()
        setUpObserver()

        // 소켓 연결
        SocketDispatcher.registerHandler(SocketManager.getSocket(), viewModel.getHandler())

        adapter = ChatRVAdapter(userId)
        binding.rvVideoChat.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvVideoChat.adapter = adapter

        binding.btnSend.setOnClickListener {
            val text = binding.etInputChat.text.toString()
            viewModel.sendMessage(roomId, nickname, text)
            binding.etInputChat.setText("")
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.joinRoom(roomId,nickname)
    }

    private fun initData(){
        val spf = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        userId = spf.getInt("userId", 0)
        nickname = spf.getString("nickname","nickname")?:"user"

        setFragmentResultListener("room_data"){ requestkey, bunlde ->

            val result  = bunlde.getParcelable<RoomData>("room_data")

            roomId = result?.roomId ?:0
            Log.d(TAG,"data ${result} / id : ${roomId}")
        }

    }

    private fun setUpObserver() {
        viewModel.chatHistory.observe(viewLifecycleOwner) { list ->
            if (list == null) return@observe
            adapter.initChatHistory(list)
        }

        viewModel.chat.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            adapter.addChat(data)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        SocketDispatcher.unregisterHandler(SocketManager.getSocket(), viewModel.getHandler())
        _binding = null
    }
}