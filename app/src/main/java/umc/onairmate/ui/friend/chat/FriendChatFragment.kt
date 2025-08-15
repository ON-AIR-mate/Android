package umc.onairmate.ui.friend.chat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.data.socket.SocketDispatcher
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.databinding.FragmentVideoChatBinding
import umc.onairmate.ui.chat_room.ChatRoomLayoutActivity
import umc.onairmate.ui.friend.FriendViewModel
import umc.onairmate.ui.home.HomeViewModel
import umc.onairmate.ui.pop_up.JoinRoomPopup
import umc.onairmate.ui.pop_up.PopupClick
import umc.onairmate.ui.util.SharedPrefUtil
import kotlin.getValue

@AndroidEntryPoint
class FriendChatFragment: Fragment() {
    private val TAG = javaClass.simpleName
    private var _binding: FragmentVideoChatBinding? = null
    private val binding get() = _binding!!
    private var user : UserData = UserData()
    private var friend : FriendData = FriendData()
    private val chatViewModel: FriendChatViewModel by viewModels()
    private val friendViewModel : FriendViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private var roomData : RoomData = RoomData()

    lateinit var adapter : FriendChatRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoChatBinding.inflate(inflater, container, false)

        initData()
        setUpObserver()
        setTextListener()

        // 소켓 연결
        val socket = SocketManager.getSocketOrNull()
        socket?.let {
            SocketDispatcher.registerHandler(it, chatViewModel.getHandler())
        }


        adapter = FriendChatRVAdapter(user, friend, object : FriendChatEventListener{
            override fun collectionClick(data: CollectionData) {
                TODO("Not yet implemented")
            }

            override fun invite(data: RoomData) {
                roomData = data

            }

        })

        binding.rvVideoChat.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.rvVideoChat.adapter = adapter

        binding.btnSend.setOnClickListener {
            val text = binding.etInputChat.text.toString()
            chatViewModel.sendMessage(friend.userId, text)
            binding.etInputChat.setText("")
        }


        chatViewModel.joinDM(friend.userId)
        return binding.root
    }

    private fun setTextListener(){
        binding.etInputChat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            override fun afterTextChanged(s: Editable?) {
                binding.btnSend.isEnabled = !binding.etInputChat.text.isEmpty()
            }
        })
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
        chatViewModel.directMessage.observe(viewLifecycleOwner) { data ->
            if (data == null) return@observe
            adapter.addChat(listOf(data))
        }
        friendViewModel.dmHistory.observe(viewLifecycleOwner) { list ->
            if (list == null) return@observe
            adapter.addChat(list)
        }
        homeViewModel.joinRoom.observe(viewLifecycleOwner){ data ->
            if (data == null) return@observe
            (activity?.supportFragmentManager?.findFragmentByTag("JoinRoomPopup")
                    as? androidx.fragment.app.DialogFragment
                    )?.dismissAllowingStateLoss()
            if(data){
                // 방 액티비티로 전환
                val intent = Intent(requireActivity(), ChatRoomLayoutActivity::class.java).apply {
                    putExtra("room_data", roomData)
                }
                startActivity(intent)
            }
            else{
                Toast.makeText(requireContext(),"방 참여에 실패했습니다.\n다시시도 해주세요", Toast.LENGTH_SHORT).show()
            }
            homeViewModel.clearJoinRoom()
        }

    }

    // 방 참여 팝업 띄우기
    private fun showJoinRoomPopup(){
        val dialog = JoinRoomPopup(roomData, object : PopupClick {
            override fun rightClickFunction() {
                homeViewModel.joinRoom(roomData.roomId)
            }
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "JoinRoomPopup")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}