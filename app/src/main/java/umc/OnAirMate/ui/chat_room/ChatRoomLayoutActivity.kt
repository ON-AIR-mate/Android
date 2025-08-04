package umc.onairmate.ui.chat_room

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.ActivityChatRoomLayoutBinding

@AndroidEntryPoint
class ChatRoomLayoutActivity : AppCompatActivity() {

    lateinit var roomData: RoomData
    private lateinit var binding: ActivityChatRoomLayoutBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        roomData = intent.getParcelableExtra("room_data", RoomData::class.java)!!
        Log.d("data", "room : ${roomData}")

        initScreen()
        onDrawerListener()
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