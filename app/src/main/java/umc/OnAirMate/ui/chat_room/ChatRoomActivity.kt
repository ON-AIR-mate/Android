package umc.onairmate.ui.chat_room

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import umc.onairmate.R
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.databinding.ActivityChatRoomBinding
import umc.onairmate.ui.home.room.HomeEventListener
import umc.onairmate.ui.home.room.RoomRVAdapter


class ChatRoomActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChatRoomBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}