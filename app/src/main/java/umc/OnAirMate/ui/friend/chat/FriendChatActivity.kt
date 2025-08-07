package umc.onairmate.ui.friend.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.ActivityFriendChatBinding

@AndroidEntryPoint
class FriendChatActivity: AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityFriendChatBinding
    private var friendId : Int =0
    private var friendNickname :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFriendChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //roomData = intent.getParcelableExtra("room_data", RoomData::class.java)!!
        friendId = intent.getIntExtra("friendId",0)
        friendNickname = intent.getStringExtra("nickname")!!

        binding.tvNickname.text = friendNickname
        binding.ivBack.setOnClickListener {
            finish()
        }


        val chatRoom = FriendChatFragment()
        //chatRoom.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fr_chat_module, chatRoom)
            .commit()

    }

}