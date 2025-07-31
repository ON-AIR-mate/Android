package umc.onairmate.ui.chat_room

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.databinding.FragmentChatRoomSidePannelBinding

class ChatRoomDrawerFragment : Fragment() {

    lateinit var binding: FragmentChatRoomSidePannelBinding
    private lateinit var adapter: ChatRoomParticipantRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomSidePannelBinding.inflate(inflater, container, false)

        setParticipantView()
        onClickMenu()

        return binding.root
    }

    private fun setParticipantView() {
        adapter = ChatRoomParticipantRVAdapter(userList)
        binding.rvParticipants.adapter = adapter
        binding.rvParticipants.layoutManager = LinearLayoutManager(context)
    }

    fun onClickMenu() {
        binding.ivMenu.setOnClickListener {
            (activity as? ChatRoomLayoutActivity)?.closeDrawer()
        }
    }
}

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