package umc.onairmate.ui.lounge.personal

import android.os.Bundle

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.databinding.ActivityPersonalLoungeBinding
import umc.onairmate.ui.chat_room.message.VideoChatFragment
import umc.onairmate.ui.lounge.bookmark.BookmarkListFragment
import umc.onairmate.ui.lounge.collection.CollectionListFragment

@AndroidEntryPoint
class PersonalLoungeActivity : AppCompatActivity() {

    // Fragment용 바인딩 클래스를 그대로 사용
    private lateinit var binding: ActivityPersonalLoungeBinding
    private val viewModel: SharedCollectionsViewModel by viewModels()
    private var friendId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalLoungeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        friendId  = intent.getIntExtra("FRIEND_ID", 0)
//        val bundle = Bundle()
//        bundle.putInt("friendId", 0)
//        val fragment =
//        fragment.arguments = bundle

        viewModel.getFriendPublicCollections(friendId)

        supportFragmentManager.beginTransaction()
            .replace(R.id.personal_lounge_content_container,  PersonalLoungeFragment())
            .commit()
    }


}