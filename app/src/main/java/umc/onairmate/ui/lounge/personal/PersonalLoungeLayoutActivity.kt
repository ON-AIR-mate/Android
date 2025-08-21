package umc.onairmate.ui.lounge.personal

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
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.socket.SocketDispatcher
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.databinding.ActivityChatRoomLayoutBinding
import umc.onairmate.databinding.ActivityMainBinding
import umc.onairmate.ui.chat_room.ChatRoomFragment
import umc.onairmate.ui.chat_room.ChatVideoViewModel
import umc.onairmate.ui.chat_room.drawer.ChatRoomDrawerFragment
import umc.onairmate.ui.chat_room.message.VideoChatViewModel
import umc.onairmate.ui.home.HomeViewModel
import kotlin.getValue
import androidx.fragment.app.commit
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.ui.lounge.bookmark.BookmarkViewModel


/**
 * PersonalLoungeLayoutActivity: 개인 라운지 화면의 메인 컨테이너 액티비티.
 *
 * 이 액티비티는 DrawerLayout을 사용하지 않습니다.
 * - 하위 프래그먼트: PersonalLoungeFragment
 *
 * Activity의 역할: 하나의 메인 프래그먼트(PersonalLoungeFragment)를 담는 컨테이너.
 * 화면 전환(PersonalLoungeFragment -> PersonalCollectionDetailFragment)은
 * PersonalLoungeFragment 내부에서 처리됩니다.
 */

@AndroidEntryPoint
class PersonalLoungeLayoutActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName
    lateinit var friendData: FriendData
    private lateinit var binding: ActivityMainBinding

    // 하위 프래그먼트에서 activtyViewModels를 사용하므로 하위 프래그먼트에서 쓰는 뷰모델을 여기서 선언
    private val bookmarkViewModel: BookmarkViewModel by viewModels()
    private val collectionViewModel: CollectionViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // fragment_container ID를 사용하여 프래그먼트 추가
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager.commit {
                replace(R.id.activity_fragment_container, PersonalLoungeFragment())
            }
        }
            // "FRIEND_ID" 키로 전달된 친구 ID를 가져옵니다.
        // 만약 null이 아니라면, 이 Activity는 개인 라운지용으로 사용되는 것입니다.
        val friendId : Int = intent.getIntExtra("FRIEND_ID",0)


        // 초기화 로직
        initScreen()
    }

    private fun initScreen() {
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, PersonalLoungeFragment())
            }

        }
    }
}