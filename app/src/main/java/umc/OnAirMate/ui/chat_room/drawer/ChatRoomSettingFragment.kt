package umc.onairmate.ui.chat_room.drawer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.databinding.FragmentChatRoomSettingBinding
import umc.onairmate.ui.chat_room.ChatRoomViewModel

val inviteOptions = listOf("방장만 허용", "모두 허용")

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

    fun initScreen() {
        // todo: room setting api에 get 추가되면 초기화 구현하기
        binding.npMaximumParticipant.minValue = 1
        binding.npMaximumParticipant.maxValue = 15

        val adapter = RoomInviteSettingSpAdapter(requireContext(), inviteOptions)
        binding.spInviteSetting.adapter = adapter
        binding.spInviteSetting.dropDownVerticalOffset = 50

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
        val position = binding.spInviteSetting.selectedItemPosition
        val currentRoomSetting = RoomSettingData(
            autoArchiving = isAutoArchived,
            invitePermission = binding.spInviteSetting.getItemAtPosition(position).toString(),
            isPrivate = isPrivate,
            maxParticipants = binding.npMaximumParticipant.value
        )

        chatRoomViewModel.setRoomSetting(roomData.roomId, currentRoomSetting)
    }
}

