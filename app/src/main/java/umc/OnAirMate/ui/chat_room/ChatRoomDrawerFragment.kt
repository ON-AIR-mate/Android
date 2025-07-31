package umc.onairmate.ui.chat_room

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.FragmentChatRoomSidePannelBinding

class ChatRoomDrawerFragment : Fragment() {

    lateinit var binding: FragmentChatRoomSidePannelBinding
    private lateinit var adapter: ChatRoomParticipantRVAdapter
    lateinit var roomData: RoomData

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // roomData = arguments?.getParcelable("room_data", RoomData::class.java)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomSidePannelBinding.inflate(inflater, container, false)

        // initScreen()
        setParticipants()
        onClickMenu()

        return binding.root
    }

    fun initScreen() {
        binding.itemRoomManager.tvUserNickname.text = roomData.hostNickname
        // todo: 썸네일 로더 이름 바꾸고 이미지 로딩하기
    }

    // 방 참가자 명단의 어댑터와 뷰 연결
    private fun setParticipants() {
        // todo: 진짜 user list는 api에서 받아오기
        adapter = ChatRoomParticipantRVAdapter(userList)
        binding.rvParticipants.adapter = adapter
        binding.rvParticipants.layoutManager = LinearLayoutManager(context)
    }

    // 좌상단 메뉴버튼 클릭시 drawer 접기
    fun onClickMenu() {
        binding.ivMenu.setOnClickListener {
            (activity as? ChatRoomLayoutActivity)?.closeDrawer()
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