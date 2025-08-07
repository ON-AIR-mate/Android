package umc.onairmate.ui.chat_room.drawer.participants

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.FragmentChatRoomParticipantsBinding
import umc.onairmate.ui.chat_room.ChatRoomViewModel

@AndroidEntryPoint
class ChatRoomParticipantsFragment : Fragment() {

    private val chatRoomViewModel: ChatRoomViewModel by viewModels()

    lateinit var binding: FragmentChatRoomParticipantsBinding

    private lateinit var adapter: ChatRoomParticipantRVAdapter
    var roomData: RoomData? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatRoomParticipantsBinding.inflate(layoutInflater)

        roomData = arguments?.getParcelable("room_data", RoomData::class.java)
        Log.d("data", "room : ${roomData}")

        initScreen()
        setParticipants()

        return binding.root
    }

    fun initScreen() {
        chatRoomViewModel.getParticipantDataInfo(roomData!!.roomId)
        binding.itemRoomManager.tvUserNickname.text = roomData!!.hostNickname
    }

    // 방 참가자 명단의 어댑터와 뷰 연결
    private fun setParticipants() {
        chatRoomViewModel.participantDataInfo.observe(viewLifecycleOwner) { data ->
            val userList = data ?: emptyList()

            adapter = ChatRoomParticipantRVAdapter(userList, object: ParticipantItemClickListener {
                override fun clickMessage(data: ParticipantData) {
                    TODO("Not yet implemented")
                }
                override fun clickReport(data: ParticipantData) {
                    TODO("Not yet implemented")
                }

                override fun clickRecommend(data: ParticipantData) {
                    TODO("Not yet implemented")
                }

                override fun clickAddFriend(data: ParticipantData) {
                    TODO("Not yet implemented")
                }

                override fun clickBlock(data: ParticipantData) {
                    TODO("Not yet implemented")
                }
            })
            binding.rvParticipants.adapter = adapter
            binding.rvParticipants.layoutManager = LinearLayoutManager(context)
        }
    }

}

// 더미데이터
val userList: List<ParticipantData> = listOf(
    ParticipantData(
        nickname = "참가자1",
        isHost = false,
        joinedAt = "",
        popularity = 0,
        profileImage = "",
        userId = 0
    ),
    ParticipantData(
        nickname = "참가자2",
        isHost = false,
        joinedAt = "",
        popularity = 0,
        profileImage = "",
        userId = 0
    ),
    ParticipantData(
        nickname = "참가자3",
        isHost = false,
        joinedAt = "",
        popularity = 0,
        profileImage = "",
        userId = 0
    ),
    ParticipantData(
        nickname = "참가자4",
        isHost = false,
        joinedAt = "",
        popularity = 0,
        profileImage = "",
        userId = 0
    )
)