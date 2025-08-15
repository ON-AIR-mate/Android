package umc.onairmate.ui.chat_room

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import umc.onairmate.ui.chat_room.message.VideoChatViewModel
import umc.onairmate.ui.home.HomeViewModel
import umc.onairmate.ui.pop_up.PopupClick
import umc.onairmate.ui.util.SharedPrefUtil

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    lateinit var roomData: RoomData
    lateinit var binding: FragmentChatRoomBinding

    private var player: YouTubePlayer? = null
    private var currentSecond: Float = 0f
    private var lastProcessedSecond: Int = 0

    val user = SharedPrefUtil.getData("user_info") ?: UserData()

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val chatRoomViewModel: ChatVideoViewModel by activityViewModels()
    private val videoChatViewModel: VideoChatViewModel by activityViewModels()

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
        onClickLeaveRoom()
        setObserver()

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

    fun setObserver() {
        // 현재 유저가 방을 나가는 api가 완료 - 액티비티를 종료
        homeViewModel.leaveRoom.observe(viewLifecycleOwner) { data ->
            if (data == true) {
                requireActivity().finish()
                homeViewModel.clearRoomDetailInfo()
            }
        }
        // 모든 유저의 방을 나가는 이벤트를 전부 수신 - 방을 나가는 사람이 방장인 경우 방 종료
        videoChatViewModel.userLeftDataInfo.observe(viewLifecycleOwner) { data ->
            Log.d("이게오냐", "${data}")
            if (data.roomParticipants.isEmpty()) {
                showTerminateRoomPopup()
                player?.pause()
            }
        }

        // 소켓 통신) participants의 플레이어 상태 변경

        // 1. 처음 입장시 host와 플레이어 싱크
        chatRoomViewModel.videoSyncDataInfo.observe(viewLifecycleOwner) { data ->
            Log.d("PlayerObserver", "sync: $data")
            if ((user.nickname != roomData.hostNickname)) {
                currentSecond = data.currentTime
            }
        }
        // 2. host가 영상 재생할 경우 플레이어 싱크
        chatRoomViewModel.videoPlayDataInfo.observe(viewLifecycleOwner) { data ->
            Log.d("PlayerObserver", "play: $data")

            if (user.nickname != roomData.hostNickname) {
                player?.seekTo(data.currentTime)
                player?.play()
            }
        }
        // 3. host가 영상 일시정지할 경우 플레이어 싱크
        chatRoomViewModel.videoPauseData.observe(viewLifecycleOwner) { data ->
            Log.d("PlayerObserver", "pause: $data")

            if (user.nickname != roomData.hostNickname) {
                player?.seekTo(data.currentTime)
                player?.pause()
            }
        }
    }

    private fun onClickSetting() {
        binding.ivSetting.setOnClickListener {
            (activity as? ChatRoomLayoutActivity)?.openDrawer()
        }
    }

    fun onClickLeaveRoom() {
        // 방 나가기 버튼 눌렀을 때 팝업 띄우기
        binding.ivGoBack.setOnClickListener {
            showLeaveRoomPopup(roomData)
        }

        // 뒤로가기 버튼 눌렀을 때도 팝업 띄우기
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showLeaveRoomPopup(roomData)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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
                youTubePlayer.loadVideo(videoId, currentSecond)

                this@ChatRoomFragment.player = youTubePlayer
            }

            // 현재 영상 재생 위치
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                if (user.nickname == roomData.hostNickname) {
                    currentSecond = second
                    if (second.toInt() != lastProcessedSecond) {

                        if ((currentSecond.toInt() - lastProcessedSecond) in -3..3) {
                            lastProcessedSecond = second.toInt()
                            chatRoomViewModel.sendVideoPlayerControl("video:sync", roomData.roomId, currentSecond)
                        } else {
                            lastProcessedSecond = second.toInt()
                            chatRoomViewModel.sendVideoPlayerControl("video:play", roomData.roomId, currentSecond)
                        }
                    }
                }
            }

            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                when (state) {
                    PlayerConstants.PlayerState.PLAYING -> {
                        // 영상 재생 - 방장->참가자 전파
                        if (user.nickname == roomData.hostNickname) {
                            val playTime = currentSecond
                            chatRoomViewModel.sendVideoPlayerControl("video:play", roomData.roomId, currentSecond)
                        }
                    }
                    PlayerConstants.PlayerState.PAUSED -> {
                        // 영상 일시정지 - 방장->참가자 전파
                        if (user.nickname == roomData.hostNickname) {
                            val pauseTime = lastProcessedSecond
                            chatRoomViewModel.sendVideoPlayerControl("video:pause", roomData.roomId, currentSecond)
                        }
                    }
                    PlayerConstants.PlayerState.ENDED -> {
                        // 영상 재생이 끝났을 때
                        showTerminateRoomPopup()
                    }
                    else -> {}
                }
            }
        }

        // disable iframe ui
        val options: IFramePlayerOptions = IFramePlayerOptions.Builder().controls(0).build()
        playerView.initialize(listener, options)
    }

    // 뒤로가기, 나가기 버튼으로 방 나가기
    private fun showLeaveRoomPopup(data: RoomData) {
        val dialog = ChatRoomLeaveDialog(data, object : PopupClick {
            override fun leftClickFunction() {
                homeViewModel.leaveRoom(roomData.roomId)
            }

            override fun rightClickFunction() {}
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "LeaveRoomPopup")
        }
    }

    // 방이 종료됨
    private fun showTerminateRoomPopup() {
        val dialog = ChatRoomTerminateDialog(object : PopupClick {
            override fun leftClickFunction() {
                if (user.nickname == roomData.hostNickname) {
                    homeViewModel.leaveRoom(roomData.roomId)
                } else {
                    requireActivity().finish()
                    homeViewModel.clearRoomDetailInfo()
                }
            }

            override fun rightClickFunction() {}
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "TerminateRoomPopup")
        }
    }

}