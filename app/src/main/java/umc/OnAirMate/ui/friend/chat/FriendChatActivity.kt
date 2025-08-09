package umc.onairmate.ui.friend.chat

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.databinding.ActivityFriendChatBinding
import umc.onairmate.ui.MainActivity

@AndroidEntryPoint
class FriendChatActivity: AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private lateinit var binding: ActivityFriendChatBinding
    private var friend : FriendData = FriendData()
    private var friendNickname :String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFriendChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()
        setView()
        initClickListener()
    }

    private fun initData(){
        val received: FriendData? = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("friendData", FriendData::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("friendData")
        }

        if (received == null) {
            setResult(Activity.RESULT_CANCELED)
            finish()
            return
        }
        friend = received
    }

    private fun setView(){
        binding.tvNickname.text = friend.nickname

        val chatRoom = FriendChatFragment()
        val bundle = Bundle()
        bundle.putParcelable("friendData", friend)
        chatRoom.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.fr_chat_module, chatRoom)
            .commit()

    }

    private fun initClickListener(){
        binding.ivYoutubeSearch.setOnClickListener {
            finishAndOpenSearch()
        }

        binding.ivNotification.setOnClickListener {

        }
        binding.ivBack.setOnClickListener {
            onCancel()
        }

    }

    private fun finishAndOpenSearch() {
        val data = Intent().apply { putExtra("go_search_video", true) }
        setResult(Activity.RESULT_OK, data)
        finish()
    }


    // 단순 취소라면 RESULT_CANCELED로 종료
    private fun onCancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

}