package umc.onairmate.ui.chat_room.drawer.setting

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.data.model.entity.InvitePermission
import umc.onairmate.data.model.entity.ParticipantPreset
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.databinding.FragmentChatRoomSettingBinding
import umc.onairmate.ui.chat_room.drawer.ChatRoomDrawerFragment
import umc.onairmate.ui.chat_room.ChatRoomViewModel
import umc.onairmate.ui.chat_room.message.VideoChatViewModel

val inviteOptions = InvitePermission.entries.map { it.label }
val maxParticipants = ParticipantPreset.entries.map { it.count.toString() }

// 이 화면은 추후 방장만 보이게 해야 함
// 이 화면으로 들어가는 버튼에서 제어 필요
@AndroidEntryPoint
class ChatRoomSettingFragment : Fragment() {

    private val chatRoomViewModel: ChatRoomViewModel by activityViewModels()
    private val videoChatViewModel: VideoChatViewModel by activityViewModels()
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
        initToggleListener()
        onClickGoBack()
        setObservers()
        return binding.root
    }

    private fun initScreen() {
        val maxParticipantsAdapter = RoomSettingSpAdapter(requireContext(), maxParticipants)
        binding.spMaximumParticipant.adapter = maxParticipantsAdapter

        val inviteSettingAdapter = RoomSettingSpAdapter(requireContext(), inviteOptions)
        binding.spInviteSetting.adapter = inviteSettingAdapter
        val settings = RoomSettingData(
            autoArchiving = roomData.autoArchiving,
            invitePermission = roomData.invitePermission,
            isPrivate = roomData.isPrivate,
            maxParticipants= roomData.maxParticipants
        )
        setRoomSettings(settings)
    }

    private fun setObservers(){
        videoChatViewModel.roomSettingDataInfo.observe(viewLifecycleOwner) { data ->
            if (data == null) {
                Toast.makeText(context, "설정을 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
            setRoomSettings(data)
        }
    }

    private fun setRoomSettings(roomSetting: RoomSettingData){
        val currentMaxParticipant = roomSetting.maxParticipants.toString()
        var position = maxParticipants.indexOf(currentMaxParticipant).takeIf { it >= 0 } ?: 0
        binding.spMaximumParticipant.setSelection(position)

        val currentInvitePreset = roomSetting.invitePermission
        position = inviteOptions.indexOf(currentInvitePreset).takeIf { it >= 0 } ?: 0
        binding.spInviteSetting.setSelection(position)

        if (roomSetting.autoArchiving) setAutoArchiveOnClickListener() else setAutoArchiveOffClickListener()
        if (roomSetting.isPrivate) setPrivateRoomOnClickListener() else setPrivateRoomOffClickListener()
    }

    fun initToggleListener() {
        setAutoArchiveOnClickListener()
        setAutoArchiveOffClickListener()
        setPrivateRoomOnClickListener()
        setPrivateRoomOffClickListener()
    }

    fun setAutoArchiveOnClickListener() {
        binding.ivAutoArchiveOff.setOnClickListener {
            binding.ivAutoArchiveOn.visibility = View.VISIBLE
            binding.ivAutoArchiveOff.visibility = View.GONE
            isAutoArchived = true
        }
    }

    fun setAutoArchiveOffClickListener() {
        binding.ivAutoArchiveOn.setOnClickListener {
            binding.ivAutoArchiveOn.visibility = View.GONE
            binding.ivAutoArchiveOff.visibility = View.VISIBLE
            isAutoArchived = false
        }
    }

    fun setPrivateRoomOnClickListener() {
        binding.ivPrivateRoomOff.setOnClickListener {
            binding.ivPrivateRoomOn.visibility = View.VISIBLE
            binding.ivPrivateRoomOff.visibility = View.GONE
            isPrivate = true
        }
    }

    fun setPrivateRoomOffClickListener() {
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

