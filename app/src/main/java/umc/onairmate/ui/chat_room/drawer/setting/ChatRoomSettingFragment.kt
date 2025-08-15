package umc.onairmate.ui.chat_room.drawer.setting

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
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
import umc.onairmate.ui.chat_room.ChatVideoViewModel
import umc.onairmate.ui.chat_room.message.VideoChatViewModel

val inviteOptions = InvitePermission.entries.map { it.displayName }
val maxParticipants = ParticipantPreset.entries.map { it.count.toString() }

// 이 화면은 추후 방장만 보이게 해야 함
// 이 화면으로 들어가는 버튼에서 제어 필요
@AndroidEntryPoint
class ChatRoomSettingFragment : Fragment() {
    private val TAG = this.javaClass.simpleName

    private val chatRoomViewModel: ChatVideoViewModel by activityViewModels()
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
        setUpObserver()
        onClickGoBack()

        return binding.root
    }

    private fun initScreen() {
        binding.spMaximumParticipant.adapter = RoomSettingSpAdapter(requireContext(), maxParticipants)
        binding.spInviteSetting.adapter = RoomSettingSpAdapter(requireContext(), inviteOptions)

        chatRoomViewModel.getRoomSetting(roomData.roomId)
    }

    private fun setUpObserver() {
        chatRoomViewModel.roomSettingDataInfo.observe(viewLifecycleOwner) { data ->
            Log.d(TAG, "${data}")
            data?.let {
                val roomSetting = data

                binding.spInviteSetting.onItemSelectedListener = null
                binding.spMaximumParticipant.onItemSelectedListener = null

                val targetPreset = ParticipantPreset.fromCount(roomSetting.maxParticipants)?.count
                val participantPosition = maxParticipants.indexOf(targetPreset.toString()) ?: 0
                binding.spMaximumParticipant.setSelection(participantPosition)

                val currentInvitePreset = InvitePermission.fromApiName(roomSetting.invitePermission)?.displayName
                val invitePosition = inviteOptions.indexOf(currentInvitePreset) ?: 0
                binding.spInviteSetting.setSelection(invitePosition)

                setSpinnerListener()

                if (roomSetting.autoArchiving) setAutoArchiveUiUpdate(true) else setAutoArchiveUiUpdate(false)
                if (roomSetting.isPrivate) setPrivateUiUpdate(true) else setPrivateUiUpdate(false)
            }
        }
    }

    private fun setSpinnerListener() {
        val itemSpinnerListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("Chatroomviewmodel", "너냐?")
                saveSetting()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.spInviteSetting.onItemSelectedListener = itemSpinnerListener
        binding.spMaximumParticipant.onItemSelectedListener = itemSpinnerListener
    }

    fun initToggleListener() {
        setAutoArchiveOnClickListener()
        setAutoArchiveOffClickListener()
        setPrivateRoomOnClickListener()
        setPrivateRoomOffClickListener()
    }

    fun setAutoArchiveUiUpdate(state: Boolean) {
        if (state) {
            binding.ivAutoArchiveOn.visibility = View.VISIBLE
            binding.ivAutoArchiveOff.visibility = View.GONE
            isAutoArchived = true
            Log.d(TAG, "isAutoArchived true")
        } else {
            binding.ivAutoArchiveOn.visibility = View.GONE
            binding.ivAutoArchiveOff.visibility = View.VISIBLE
            isAutoArchived = false
            Log.d(TAG, "isAutoArchived false")
        }
    }

    fun setPrivateUiUpdate(state: Boolean) {
        if (state) {
            binding.ivPrivateRoomOn.visibility = View.VISIBLE
            binding.ivPrivateRoomOff.visibility = View.GONE
            isPrivate = true
            Log.d(TAG, "isPrivate true")
        } else {
            binding.ivPrivateRoomOn.visibility = View.GONE
            binding.ivPrivateRoomOff.visibility = View.VISIBLE
            isPrivate = false
            Log.d(TAG, "isPrivate false")
        }
    }

    fun setAutoArchiveOnClickListener() {
        binding.ivAutoArchiveOff.setOnClickListener {
            setAutoArchiveUiUpdate(true)
            saveSetting()
        }
    }

    fun setAutoArchiveOffClickListener() {
        binding.ivAutoArchiveOn.setOnClickListener {
            setAutoArchiveUiUpdate(false)
            saveSetting()
        }
    }

    fun setPrivateRoomOnClickListener() {
        binding.ivPrivateRoomOff.setOnClickListener {
            setPrivateUiUpdate(true)
            saveSetting()
        }
    }

    fun setPrivateRoomOffClickListener() {
        binding.ivPrivateRoomOn.setOnClickListener {
            setPrivateUiUpdate(false)
            saveSetting()
        }
    }

    fun onClickGoBack() {
        binding.ivGoBack.setOnClickListener {
            saveSetting()
            val parent = parentFragment as? ChatRoomDrawerFragment
            parent?.changeFrameToParticipant()
        }
    }

    private fun saveSetting() {
        // 각 스피너에서 선택된 인덱스를 추출
        val inviteSettingPosition = binding.spInviteSetting.selectedItemPosition
        val maxParticipantPosition = binding.spMaximumParticipant.selectedItemPosition

        // api에 put할 데이터를 정제
        // 스피너: 각 스피너에서 사용된 리스트[추출된 인덱스]
        // 토글 버튼: 각 버튼과 연동된 bool 변수
        val currentRoomSetting = RoomSettingData(
            autoArchiving = isAutoArchived,
            invitePermission = InvitePermission.fromDisplayName(inviteOptions[inviteSettingPosition])!!.apiName,
            isPrivate = isPrivate,
            maxParticipants = maxParticipants[maxParticipantPosition].toInt()
        )

        Log.d("Chatroomviewmodel", "currentSetting: ${currentRoomSetting}")

        // api에 put 하기
        chatRoomViewModel.setRoomSetting(roomData.roomId, currentRoomSetting)
    }
}

