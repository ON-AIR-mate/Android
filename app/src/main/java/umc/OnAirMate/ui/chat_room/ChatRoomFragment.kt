package umc.onairmate.ui.chat_room

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.FragmentChatRoomBinding
import umc.onairmate.ui.chat_room.message.VideoChatFragment
import umc.onairmate.ui.home.SearchRoomViewModel

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    lateinit var roomData: RoomData
    lateinit var binding: FragmentChatRoomBinding

    private val searchRoomViewModel: SearchRoomViewModel by viewModels()

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

        initChat()
        initPlayer()
        initScreen()
        onClickSetting()
        onClickGoBack()

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

    fun onClickGoBack() {
        binding.ivGoBack.setOnClickListener {
            searchRoomViewModel.leaveRoom(roomData.roomId)
            requireActivity().finish()
        }
    }

    // 유튜브 모듈
    // 근데.. api에 영상 id 받는 부분이 없어보임..
    fun initPlayer() {
        val youtubePlayer = binding.youtubePlayer
        lifecycle.addObserver(youtubePlayer)

        youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // todo: 영상 id 오면 받아서 넣기
                val videoId = "CgCVZdcKcqY"
                youTubePlayer.loadVideo(videoId, 0f) // todo: RoomData duration 연동
            }
        })
    }

    // 채팅창에 번들 전달
    private fun initChat() {
        val bundle = Bundle()
        bundle.putParcelable("room_data", roomData)
        setFragmentResult("room_data", bundle)
//        val chat = VideoChatFragment()
//        chat.arguments = bundle
//        requireActivity().getSupportFragmentManager().beginTransaction()
//            .replace(R.id.fragment_container, chat)
//            .commit()

    }
}