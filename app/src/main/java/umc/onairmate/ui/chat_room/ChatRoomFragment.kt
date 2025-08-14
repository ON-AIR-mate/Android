package umc.onairmate.ui.chat_room

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.pierfrancescosoffritti.androidyoutubeplayer.core.customui.DefaultPlayerUiController
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.databinding.FragmentChatRoomBinding
import umc.onairmate.ui.chat_room.message.VideoChatFragment
import umc.onairmate.ui.home.HomeViewModel
import umc.onairmate.ui.util.SharedPrefUtil

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    lateinit var roomData: RoomData
    lateinit var binding: FragmentChatRoomBinding

    private var player: YouTubePlayer? = null
    private var currentSecond: Float = 0f
    private var lastProcessedSecond: Int = 0

    val user = SharedPrefUtil.getData("user_info") ?: UserData()

    private val searchRoomViewModel: HomeViewModel by viewModels()
    private val chatRoomViewModel: ChatVideoViewModel by viewModels()

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

        initPlayer()
        initScreen()
        onClickSetting()
        onClickGoBack()
        initChat()
        onEventListener()
        return binding.root
    }

    fun initScreen() {
        binding.tvRoomTitle.text = roomData.roomTitle

        val bundle = Bundle()
        bundle.putInt("roomId", roomData.roomId)

        val chatRoom = VideoChatFragment()
        chatRoom.arguments = bundle

        childFragmentManager.beginTransaction()
            .replace(R.id.fg_chat_module, chatRoom)
            .commit()

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
    fun initPlayer() {
        val playerView = binding.youtubePlayer
        lifecycle.addObserver(playerView)

        val listener : YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // set custom ui
                val playerUiController = DefaultPlayerUiController(playerView, youTubePlayer)
                playerUiController.showFullscreenButton(false)
                playerUiController.showVideoTitle(false)
                if (user.nickname == roomData.hostNickname) {
                    playerUiController.showPlayPauseButton(true)
                    playerUiController.showSeekBar(true)
                } else {
                    playerUiController.showPlayPauseButton(false)
                    playerUiController.showSeekBar(false)
                }
                playerUiController.showYouTubeButton(false)
                playerView.setCustomPlayerUi(playerUiController.rootView)

                val videoId = roomData.videoId ?: "CgCVZdcKcqY"
                youTubePlayer.loadVideo(videoId, 0f)

                this@ChatRoomFragment.player = youTubePlayer
            }

            // 현재 영상 재생 위치
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                if (user.nickname == roomData.hostNickname) {
                    currentSecond = second
                    if (second.toInt() != lastProcessedSecond) {
                        lastProcessedSecond = second.toInt()
                        Log.d("PlayerHost", "재생중: ${lastProcessedSecond}초")
                        chatRoomViewModel.sendVideoPlayerControl("video:sync", roomData.roomId, currentSecond)
                    }
                }
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                when (state) {
                    PlayerConstants.PlayerState.ENDED -> {
                        // 영상 재생이 끝났을 때
                    }
                    PlayerConstants.PlayerState.PLAYING -> {
                        // 영상 재생 - 방장->참가자 전파
                        if (user.nickname == roomData.hostNickname) {
                            val playTime = currentSecond
                            Log.d("PlayerHost", "재생 시작: ${playTime}초")
                            chatRoomViewModel.sendVideoPlayerControl("video:play", roomData.roomId, currentSecond)
                        }
                    }
                    PlayerConstants.PlayerState.PAUSED -> {
                        // 영상 일시정지 - 방장->참가자 전파
                        if (user.nickname == roomData.hostNickname) {
                            val pauseTime = lastProcessedSecond
                            Log.d("PlayerHost", "멈춤: ${pauseTime}초")
                            chatRoomViewModel.sendVideoPlayerControl("video:pause", roomData.roomId, currentSecond)
                        }
                    }
                    else -> {}
                }
            }
        }

        // disable iframe ui
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
        playerView.initialize(listener, options)
    }

    // 채팅창에 번들 전달
    private fun initChat() {
        val bundle = Bundle()
        bundle.putParcelable("room_data", roomData)
        setFragmentResult("room_data", bundle)

    }

    private fun onEventListener() {
        chatRoomViewModel.videoSyncDataInfo.observe(viewLifecycleOwner) { data ->
            if (user.nickname != roomData.hostNickname) {
                player?.seekTo(data.currentTime)
                if (data.status == "playing") player?.play()
                else player?.pause()
            }
        }
        chatRoomViewModel.videoPlayDataInfo.observe(viewLifecycleOwner) { data ->
            if (user.nickname != roomData.hostNickname) {
                val elapsed = (System.currentTimeMillis() - data.updatedAt) / 1000.0
                player?.seekTo(((data.currentTime + elapsed) * 1000).toFloat())
                player?.play()
            }
        }
        chatRoomViewModel.videoPauseData.observe(viewLifecycleOwner) { data ->
            if (user.nickname != roomData.hostNickname) {
                player?.seekTo(data.currentTime)
                player?.pause()
            }
        }
    }
}