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
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.socket.SocketDispatcher
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.databinding.ActivityChatRoomLayoutBinding
import umc.onairmate.ui.chat_room.drawer.ChatRoomDrawerFragment
import umc.onairmate.ui.chat_room.message.VideoChatViewModel
import kotlin.getValue

@AndroidEntryPoint
class ChatRoomLayoutActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    lateinit var roomData: RoomData
    private lateinit var binding: ActivityChatRoomLayoutBinding

    private val chatRoomViewModel: ChatRoomViewModel by viewModels()
    private val videoChatViewModel: VideoChatViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        connectSocket()
        roomData = intent.getParcelableExtra("room_data", RoomData::class.java)!!
        Log.d("data", "room : ${roomData}")
        setObserver()
        initScreen()
        onDrawerListener()
    }

    private fun connectSocket() {
        // 소켓 연결
        val socket = SocketManager.getSocketOrNull()
        if (socket?.connected() == true) {
            SocketDispatcher.registerHandler(socket, videoChatViewModel.getHandler())
        }

    }
    private fun setObserver(){
        videoChatViewModel.isUserNumChanged.observe(this){data ->
            if (data== null) return@observe
            Log.d(TAG,"UserChange")
            if (data) chatRoomViewModel.getParticipantDataInfo(roomData.roomId)
        }
    }

    private fun initScreen() {
        val bundle = Bundle()
        bundle.putParcelable("room_data", roomData)

        val chatRoom = ChatRoomFragment()
        val drawer = ChatRoomDrawerFragment()

        chatRoom.arguments = bundle
        drawer.arguments = bundle

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, chatRoom)
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_drawer, drawer)
            .commit()
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }

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