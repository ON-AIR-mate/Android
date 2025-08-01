package umc.onairmate.ui.chat_room.drawer

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.InvitePermission
import umc.onairmate.data.model.entity.ParticipantPreset
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.databinding.FragmentChatRoomSettingBinding
import umc.onairmate.ui.chat_room.ChatRoomDrawerFragment
import umc.onairmate.ui.chat_room.ChatRoomViewModel

val inviteOptions = InvitePermission.entries.map { it.label }
val maxParticipants = ParticipantPreset.entries.map { it.count.toString() }

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
        handleToggle()
        onClickGoBack()

        return binding.root
    }

    private fun initScreen() {
        val maxParticipantsAdapter = RoomSettingSpAdapter(requireContext(), maxParticipants)
        binding.spMaximumParticipant.adapter = maxParticipantsAdapter

        val inviteSettingAdapter = RoomSettingSpAdapter(requireContext(), inviteOptions)
        binding.spInviteSetting.adapter = inviteSettingAdapter

        chatRoomViewModel.getRoomSetting(roomData.roomId)

        chatRoomViewModel.roomSettingDataInfo.observe(viewLifecycleOwner) { data ->
            val roomSetting = data ?: RoomSettingData()

            val currentMaxParticipant = roomSetting.maxParticipants.toString()
            var position = maxParticipants.indexOf(currentMaxParticipant).takeIf { it >= 0 } ?: 0
            binding.spMaximumParticipant.setSelection(position)

            val currentInVitePreset = roomSetting.invitePermission
            position = inviteOptions.indexOf(currentInVitePreset).takeIf { it >= 0 } ?: 0
            binding.spInviteSetting.setSelection(position)

            if (roomSetting.autoArchiving) enableAutoArchive() else disableAutoArchive()
            if (roomSetting.isPrivate) enablePrivateRoom() else disablePrivateRoom()
        }
    }

    fun handleToggle() {
        enableAutoArchive()
        disableAutoArchive()
        enablePrivateRoom()
        disablePrivateRoom()
    }

    fun enableAutoArchive() {
        binding.ivAutoArchiveOff.setOnClickListener {
            binding.ivAutoArchiveOn.visibility = View.VISIBLE
            binding.ivAutoArchiveOff.visibility = View.GONE
            isAutoArchived = true
        }
    }

    fun disableAutoArchive() {
        binding.ivAutoArchiveOn.setOnClickListener {
            binding.ivAutoArchiveOn.visibility = View.GONE
            binding.ivAutoArchiveOff.visibility = View.VISIBLE
            isAutoArchived = false
        }
    }

    fun enablePrivateRoom() {
        binding.ivPrivateRoomOff.setOnClickListener {
            binding.ivPrivateRoomOn.visibility = View.VISIBLE
            binding.ivPrivateRoomOff.visibility = View.GONE
            isPrivate = true
        }
    }

    fun disablePrivateRoom() {
        binding.ivPrivateRoomOn.setOnClickListener {
            binding.ivPrivateRoomOn.visibility = View.GONE
            binding.ivPrivateRoomOff.visibility = View.VISIBLE
            isPrivate = false
        }
    }

    fun onClickGoBack() {
        binding.ivGoBack.setOnClickListener {
            val parent = parentFragment as? ChatRoomDrawerFragment
            parent?.changeFrameToParticipant()
        }
    }

    override fun onPause() {
        super.onPause()

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

