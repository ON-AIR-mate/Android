package umc.onairmate.ui.chat_room

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.R
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.FragmentChatRoomSidePannelBinding
import umc.onairmate.ui.chat_room.drawer.ChatRoomParticipantRVAdapter
import umc.onairmate.ui.chat_room.drawer.ChatRoomParticipantsFragment
import umc.onairmate.ui.chat_room.drawer.ChatRoomSettingFragment

class ChatRoomDrawerFragment : Fragment() {

    lateinit var binding: FragmentChatRoomSidePannelBinding
    lateinit var roomData: RoomData
    private val bundle = Bundle()

    private var isSettingScreen = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomData = arguments?.getParcelable("room_data", RoomData::class.java)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomSidePannelBinding.inflate(inflater, container, false)

        initScreen()
        onClickMenu()
        onClickSetting()

        return binding.root
    }

    fun initScreen() {
        isSettingScreen = false
        bundle.putParcelable("room_data", roomData)
        changeFrameToParticipant()
    }

    fun onClickSetting() {
        binding.ivSetting.setOnClickListener {
            bundle.putParcelable("room_data", roomData)

            if (isSettingScreen == false) {
                isSettingScreen = true

                changeFrameToSetting()
            } else {
                isSettingScreen = false

                changeFrameToParticipant()
            }
        }
    }

    fun changeFrameToParticipant() {
        val participants = ChatRoomParticipantsFragment()
        participants.arguments = bundle

        childFragmentManager.beginTransaction()
            .replace(R.id.fl_drawer_layout, participants)
            .commit()
    }

    fun changeFrameToSetting() {
        val setting = ChatRoomSettingFragment()
        setting.arguments = bundle

        childFragmentManager.beginTransaction()
            .replace(R.id.fl_drawer_layout, setting)
            .commit()
    }

    // 좌상단 메뉴버튼 클릭시 drawer 접기
    fun onClickMenu() {
        binding.ivMenu.setOnClickListener {
            (activity as? ChatRoomLayoutActivity)?.closeDrawer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isSettingScreen = false
    }
}

