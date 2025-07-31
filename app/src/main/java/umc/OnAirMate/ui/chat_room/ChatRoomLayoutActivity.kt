package umc.onairmate.ui.chat_room

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.ActivityChatRoomLayoutBinding

class ChatRoomLayoutActivity : AppCompatActivity() {

    lateinit var roomData: RoomData
    private lateinit var binding: ActivityChatRoomLayoutBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatRoomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initScreen()

        roomData = intent.getParcelableExtra("room_data", RoomData::class.java)!!
    }

    private fun initScreen() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ChatRoomFragment())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_drawer, ChatRoomDrawerFragment())
            .commit()
    }

    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }

    fun closeDrawer() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
    }
}