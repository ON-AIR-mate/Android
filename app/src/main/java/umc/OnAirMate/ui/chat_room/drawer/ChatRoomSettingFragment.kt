package umc.onairmate.ui.chat_room.drawer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.databinding.FragmentChatRoomSettingBinding
import umc.onairmate.ui.chat_room.ChatRoomViewModel

val inviteOptions = listOf("방장만 허용", "모두 허용")
val maxParticipants = listOf("8", "15", "30")

// 이 화면은 추후 방장만 보이게 해야 함
// 이 화면으로 들어가는 버튼에서 제어 필요
@AndroidEntryPoint
class ChatRoomSettingFragment : Fragment() {

    private val chatRoomViewModel: ChatRoomViewModel by viewModels()
    lateinit var binding: FragmentChatRoomSettingBinding
    lateinit var roomData: RoomData

    var isAutoArchived : Boolean = true
    var isPrivate : Boolean = false

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatRoomSettingBinding.inflate(layoutInflater)
        roomData = arguments?.getParcelable("room_data", RoomData::class.java)!!

        initScreen()
        onAutoArchiveButtonClick()
        onPrivateRoomButtonClick()

        return binding.root
    }

    private fun initScreen() {
        // todo: room setting api에 get 추가되면 초기화 구현하기
        val maxParticipantsAdapter = RoomSettingSpAdapter(requireContext(), maxParticipants)
        binding.spMaximumParticipant.adapter = maxParticipantsAdapter
        //binding.spInviteSetting.dropDownVerticalOffset = 50

        val inviteSettingAdapter = RoomSettingSpAdapter(requireContext(), inviteOptions)
        binding.spInviteSetting.adapter = inviteSettingAdapter
        //binding.spInviteSetting.dropDownVerticalOffset = 50
    }

    fun onAutoArchiveButtonClick() {
        binding.ivAutoArchiveOn.setOnClickListener {
            binding.ivAutoArchiveOn.visibility = View.GONE
            binding.ivAutoArchiveOff.visibility = View.VISIBLE
            isAutoArchived = false
        }
        binding.ivAutoArchiveOff.setOnClickListener {
            binding.ivAutoArchiveOn.visibility = View.VISIBLE
            binding.ivAutoArchiveOff.visibility = View.GONE
            isAutoArchived = true
        }
    }

    fun onPrivateRoomButtonClick() {
        binding.ivPrivateRoomOn.setOnClickListener {
            binding.ivPrivateRoomOn.visibility = View.GONE
            binding.ivPrivateRoomOff.visibility = View.VISIBLE
            isPrivate = false
        }
        binding.ivPrivateRoomOff.setOnClickListener {
            binding.ivPrivateRoomOn.visibility = View.VISIBLE
            binding.ivPrivateRoomOff.visibility = View.GONE
            isPrivate = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // 각 스피너에서 선택된 인덱스를 추출
        val inviteSettingPosition = binding.spInviteSetting.selectedItemPosition
        val maxParticipantPosition = binding.spMaximumParticipant.selectedItemPosition

        // api에 put할 데이터를 정제
        // 스피너: 각 스피너에서 사용된 리스트[추출된 인덱스]
        // 토글 버튼: 각 버튼과 연동된 bool 변수
        val currentRoomSetting = RoomSettingData(
            autoArchiving = isAutoArchived,
            invitePermission = inviteOptions[inviteSettingPosition],
            isPrivate = isPrivate,
            maxParticipants = maxParticipants[maxParticipantPosition].toInt()
        )

        // api에 put 하기
        chatRoomViewModel.setRoomSetting(roomData.roomId, currentRoomSetting)
    }
}

