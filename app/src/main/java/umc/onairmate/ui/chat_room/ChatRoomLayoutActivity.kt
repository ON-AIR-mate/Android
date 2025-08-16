package umc.onairmate.ui.chat_room

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.socket.SocketDispatcher
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.databinding.ActivityChatRoomLayoutBinding
import umc.onairmate.ui.chat_room.drawer.ChatRoomDrawerFragment
import umc.onairmate.ui.chat_room.message.VideoChatViewModel
import umc.onairmate.ui.home.HomeViewModel
import kotlin.getValue

/**
 * ChatRoomLayoutActivity: 비디오 채팅방의 가장 바깥 activty
 * - 리소스: activity_chat_room_layout
 * - intent로 RoomData를 넘겨 받아야 함.
 * - 하위 프래그먼트 1: ChatRoomFragment (DrawerLayout의 배경 프래그먼트)
 * - 하위 프래그먼트 2: ChatRoomDrawerFragment (DrawerLayout의 drawer 프래그먼트)
 */
@AndroidEntryPoint
class ChatRoomLayoutActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    lateinit var roomData: RoomData
    private lateinit var binding: ActivityChatRoomLayoutBinding

    // 하위 프래그먼트에서 activtyViewModels를 사용하므로 하위 프래그먼트에서 쓰는 뷰모델을 여기서 선언
    private val chatRoomViewModel: ChatVideoViewModel by viewModels()
    private val videoChatViewModel: VideoChatViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 소켓 연결
        connectSocket()

        // roomData 받아오기
        roomData = intent.getParcelableExtra("room_data", RoomData::class.java)!!

        // 초기화 로직
        setObserver()
        initScreen()
        onDrawerListener()
    }

    override fun onPause() {
        super.onPause()
        // 소켓 핸들러 unregister
        SocketManager.getSocketOrNull()?.let { socket ->
            if (socket.connected()) {
                videoChatViewModel.leaveRoom(roomData.roomId) // emit leaveRoom
                SocketDispatcher.unregisterHandler(socket, videoChatViewModel.getHandler())
                SocketDispatcher.unregisterHandler(socket, chatRoomViewModel.getHandler())
            }
        }
    }

    private fun connectSocket() {
        // 소켓 연결
        val socket = SocketManager.getSocketOrNull()
        if (socket?.connected() == true) {
            SocketDispatcher.registerHandler(socket, videoChatViewModel.getHandler())
            SocketDispatcher.registerHandler(socket, chatRoomViewModel.getHandler())
        }

    }
    private fun setObserver(){
        videoChatViewModel.isUserNumChanged.observe(this){data ->
            if (data== null) return@observe
            Log.d(TAG,"UserChange")
            if (data) chatRoomViewModel.getParticipantDataInfo(roomData.roomId)
        }
    }

    // Drawer Layout의 배경 프래그먼트와 drawer 프래그먼트 지정
    private fun initScreen() {
        val bundle = Bundle()
        bundle.putParcelable("room_data", roomData)

        val chatRoom = ChatRoomFragment()
        val drawer = ChatRoomDrawerFragment()

        chatRoom.arguments = bundle
        drawer.arguments = bundle

        // 배경 프래그먼트 연결
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, chatRoom)
            .commit()

        // drawer 프래그먼트 연결
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_drawer, drawer)
            .commit()
    }

    // 하위 프래그먼트에서 drawer 열기 동작
    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }

    // 하위 프래그먼트에서 drawer 닫기 동작
    fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
    }

    fun onDrawerListener() {
        binding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            val container = binding.fragmentContainer.root

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {
                // container 포커스 뺏기
                container.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS
                container.isFocusable = false
                container.isFocusableInTouchMode = false
            }

            override fun onDrawerClosed(drawerView: View) {
                // container 포커스 돌려놓기
                container.importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_AUTO
                container.isFocusable = true
                container.isFocusableInTouchMode = true
            }

            override fun onDrawerStateChanged(newState: Int) {}
        })
    }
}