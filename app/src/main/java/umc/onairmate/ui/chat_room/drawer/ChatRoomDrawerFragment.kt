package umc.onairmate.ui.chat_room.drawer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.request.FriendInviteRequest
import umc.onairmate.databinding.FragmentChatRoomSidePannelBinding
import umc.onairmate.databinding.PopupInviteFriendBinding
import umc.onairmate.ui.chat_room.ChatRoomLayoutActivity
import umc.onairmate.ui.chat_room.drawer.participants.ChatRoomParticipantsFragment
import umc.onairmate.ui.chat_room.drawer.setting.ChatRoomSettingFragment
import umc.onairmate.ui.friend.FriendViewModel

@AndroidEntryPoint
class ChatRoomDrawerFragment : Fragment() {

    private val friendViewModel: FriendViewModel by viewModels()

    lateinit var binding: FragmentChatRoomSidePannelBinding
    lateinit var roomData: RoomData
    private val bundle = Bundle()

    private var isSettingScreen = false
    private var invitePopupWindow: PopupWindow? = null
    private var invitePopupRVAdapter: InviteFriendRVAdapter? = null

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
        observeFriendList()

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
        binding.ivAddUser.setOnClickListener {
            showInviteFriendPopup(it)
        }
    }

    fun showInviteFriendPopup(anchorView: View) {
        val popupBinding = PopupInviteFriendBinding.inflate(LayoutInflater.from(anchorView.context))

        invitePopupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // 1. 친구 리스트를 api로 받아오기
        friendViewModel.getFriendList()

        // 2. 친구 리스트를 리사이클러뷰 어댑터로 연결
        val recyclerView = popupBinding.rvInviteFriend
        recyclerView.layoutManager = LinearLayoutManager(anchorView.context)
        recyclerView.adapter = invitePopupRVAdapter

        // 팝업을 anchorView(여기서는 버튼) 아래에 표시합니다.
        invitePopupWindow?.showAsDropDown(anchorView)
        // 5. profit!!
    }

    fun observeFriendList() {
        friendViewModel.friendList.observe(viewLifecycleOwner) { data ->
            val friendList = data ?: emptyList()

            invitePopupRVAdapter = InviteFriendRVAdapter(friendList, {
                val body = FriendInviteRequest(roomData.roomId)
                // 3. 친구 초대 api 요청
                friendViewModel.inviteFriend(it.userId, body)
                // 4. 초대 이후 로직 (메시지를 띄운다던가..)
                Toast.makeText(this.context, "${it.nickname}님에게 초대를 발송했습니다.", Toast.LENGTH_SHORT).show()
                invitePopupWindow?.dismiss()
            })
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
        invitePopupWindow?.dismiss()
        invitePopupWindow = null
        invitePopupRVAdapter = null
    }
}

