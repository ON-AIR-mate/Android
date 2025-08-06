package umc.onairmate.ui.chat_room.message

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.FragmentVideoChatBinding
import kotlin.getValue

@AndroidEntryPoint
class VideoChatFragment: Fragment() {
    private val TAG = javaClass.simpleName
    private var _binding: FragmentVideoChatBinding? = null
    private val binding get() = _binding!!
    private var userId : Int = 0
    private var roomId : Int = 0
    lateinit var adapter : ChatRVAdapter
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoChatBinding.inflate(inflater, container, false)

        initData()
        setUpObserver()

        adapter = ChatRVAdapter(id)
        binding.rvVideoChat.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvVideoChat.adapter = adapter

        binding.btnSend.setOnClickListener {
            val text = binding.etInputChat.text.toString()
            viewModel.sendMessage(roomId,text)
            binding.etInputChat.setText("")
        }

        viewModel.getChatHistory(roomId)


        return binding.root
    }
    private fun initData(){
        val spf = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        userId = spf.getInt("userId", 0)

        setFragmentResultListener("room_data"){ requestkey, bunlde ->
            val result  = bunlde.getParcelable<RoomData>("room_data")
            roomId = result?.roomId ?:0
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
        _binding = null
    }
}