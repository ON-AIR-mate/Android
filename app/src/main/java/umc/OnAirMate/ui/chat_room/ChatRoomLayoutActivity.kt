package umc.onairmate.ui.chat_room

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import umc.onairmate.databinding.ActivityChatRoomLayoutBinding

class ChatRoomLayoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatRoomLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatRoomLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}