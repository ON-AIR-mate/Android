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
import umc.onairmate.data.model.request.SummaryCreateRequest
import umc.onairmate.databinding.FragmentChatRoomBinding
import umc.onairmate.ui.chat_room.message.VideoChatFragment
import umc.onairmate.ui.chat_room.message.VideoChatViewModel
import umc.onairmate.ui.chat_room.summary.SummaryFragment
import umc.onairmate.ui.home.HomeViewModel
import umc.onairmate.ui.pop_up.PopupClick
import umc.onairmate.ui.util.SharedPrefUtil

/**
 * ChatRoomFragment: ChatRoomLayoutActivity의 배경 프래그먼트
 * - 리소스: chat_room_fragment
 * - 번들로 RoomData를 넘겨 받아야 함
 * - 상위 액티비티: ChatRoomLayoutActivity (Drawer 레이아웃)
 * - 하위 프래그먼트: VideoChatFragment (채팅 화면)
 */
@AndroidEntryPoint
class ChatRoomFragment : Fragment() {

    lateinit var roomData: RoomData
    lateinit var binding: FragmentChatRoomBinding

    // 유튜브 플레이어 관련 변수
    private var player: YouTubePlayer? = null   // 유튜브 플레이어 객체
    private var currentSecond: Float = 0f       // 유튜브 플레이어의 현재 시간 (초단위)
    private var lastProcessedSecond: Int = 0    // 유튜브 플레이어의 이전 소켓 처리 시간 (초단위)

    // 현재 유저의 정보
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

        // 방
        initPlayer()
        initScreen()
        onClickSetting()
        onClickLeaveRoom()
        setObserver()

        return binding.root
    }

    fun initScreen() {
        // 뷰 초기화
        binding.tvRoomTitle.text = roomData.roomTitle

        // 채팅 화면과 연결
        val bundle = Bundle()
        bundle.putInt("roomId", roomData.roomId)

        val chatRoom = VideoChatFragment()
        chatRoom.arguments = bundle

        childFragmentManager.beginTransaction()
            .replace(R.id.fg_chat_module, chatRoom)
            .commit()
    }

    // ai 요약 불러오기
    fun setupSummaryScreen() {
        // ai 요약 화면과 연결
        val bundle = Bundle()
        bundle.putParcelable("roomData", roomData)

        val summary = SummaryFragment()
        summary.arguments = bundle

        childFragmentManager.beginTransaction()
            .replace(R.id.fg_chat_module, summary)
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

        // 로딩 프로그래스바
        homeViewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    // 햄버거 메뉴 클릭시 drawer 열기
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

    // 유튜브 모듈 지정
    fun initPlayer() {
        val playerView = binding.youtubePlayer
        lifecycle.addObserver(playerView)

        // 유튜브 플레이어 리스너
        val listener : YouTubePlayerListener = object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                // 유튜브 플레이어 커스텀 ui
                val playerUiController = DefaultPlayerUiController(playerView, youTubePlayer)

                playerUiController.showFullscreenButton(false)      // 전체화면 버튼 비활성화
                playerUiController.showVideoTitle(false)            // 영상 제목 비활성화 (유튜브 앱으로 연결됨)
                playerUiController.showYouTubeButton(false)         // 유튜브 버튼 비활성화 (유튜브 앱으로 연결됨)

                if (user.nickname == roomData.hostNickname) {
                    // host만 재생, 정지, 프로그래스바 조작 가능
                    playerUiController.showPlayPauseButton(true)    // 재생/정지 버튼 활성화
                    playerUiController.showSeekBar(true)            // 프로그래스바 활성화
                } else {
                    // host가 아니면 재생, 정지, 프로그래스바 조작 불가능
                    playerUiController.showPlayPauseButton(false)   // 재생/정지 버튼 비활성화
                    playerUiController.showSeekBar(false)           // 프로그래스바 비활성화
                }

                // 플레이어 뷰에 커스텀 ui 지정
                playerView.setCustomPlayerUi(playerUiController.rootView)

                // 방 데이터의 동영상 정보를 플레이어에 연동
                val videoId = roomData.videoId ?: "CgCVZdcKcqY"
                youTubePlayer.loadVideo(videoId, currentSecond)

                // 프래그먼트에서 플레이어 조작할 수 있게 지정
                this@ChatRoomFragment.player = youTubePlayer
            }

            // 현재 영상 재생 위치 (0.1초마다 실행됨)
            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                // host의 재생 시점 동기화
                if (user.nickname == roomData.hostNickname) {
                    currentSecond = second

                    // 1초마다 소켓에 emit
                    if (second.toInt() != lastProcessedSecond) {
                        if ((currentSecond.toInt() - lastProcessedSecond) in -3..3) {
                            // 1. 새로 들어온 유저의 플레이어 동기화
                            lastProcessedSecond = second.toInt()
                            chatRoomViewModel.sendVideoPlayerControl("video:sync", roomData.roomId, currentSecond)
                        } else {
                            // 2. host의 프로그래스바 조작 동기화
                            lastProcessedSecond = second.toInt()
                            chatRoomViewModel.sendVideoPlayerControl("video:play", roomData.roomId, currentSecond)
                        }
                    }
                }
            }

            // 유튜브 플레이어의 state 감지해 소켓 통신
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
                        // 영상 재생이 끝났을 때 -> 방 끝남 팝업
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

    // 방 나가기 팝업 띄우기
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

    // 방이 종료됨 팝업 띄우기
    // 방 종료 조건: 방장강제종료/영상끝남 -> 요약 화면에서 나가기 처리
    private fun showTerminateRoomPopup() {
        setupSummaryScreen()
        val dialog = ChatRoomTerminateDialog(object : PopupClick {
            override fun leftClickFunction() {
                /*if (user.nickname == roomData.hostNickname) {
                    // host일 경우 api 통신으로 방 삭제
                    homeViewModel.leaveRoom(roomData.roomId)
                } else {
                    // 일반 참가자일 경우 이미 방이 사라졌으므로 화면만 전환
                    requireActivity().finish()
                    homeViewModel.clearRoomDetailInfo()
                }*/
            }

            override fun rightClickFunction() {}
        })
        activity?.supportFragmentManager?.let { fm ->
            dialog.show(fm, "TerminateRoomPopup")
        }
    }

}