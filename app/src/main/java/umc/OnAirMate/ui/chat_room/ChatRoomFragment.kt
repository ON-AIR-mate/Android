package umc.onairmate.ui.chat_room

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.FragmentChatRoomBinding


class ChatRoomFragment : Fragment() {

    lateinit var roomData: RoomData
    lateinit var binding: FragmentChatRoomBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // roomData = arguments?.getParcelable("room_data", RoomData::class.java)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false)

        initPlayer()
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

    // 유튜브 모듈
    // 근데.. api에 영상 id 받는 부분이 없어보임..
    fun initPlayer() {
        val youtubePlayer = binding.youtubePlayer
        lifecycle.addObserver(youtubePlayer)

        youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = "S0Q4gqBUs7c"
                youTubePlayer.loadVideo(videoId, 0f) // todo: RoomData duration 연동
            }
        })
    }
}