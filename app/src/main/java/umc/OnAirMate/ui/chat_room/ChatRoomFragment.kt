package umc.onairmate.ui.chat_room

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.FragmentChatRoomBinding

class ChatRoomFragment : Fragment() {

    lateinit var roomData: RoomData
    lateinit var binding: FragmentChatRoomBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomData = arguments?.getParcelable("room_data", RoomData::class.java)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false)

        // initScreen()
        onClickSetting()

        return binding.root
    }

    fun initScreen() {
        binding.tvRoomTitle.text = roomData.roomTitle
    }

    private fun onClickSetting() {
        binding.ivSetting.setOnClickListener {
            (activity as? ChatRoomLayoutActivity)?.openDrawer()
        }
    }
}