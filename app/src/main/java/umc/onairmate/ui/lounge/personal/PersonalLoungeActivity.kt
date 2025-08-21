package umc.onairmate.ui.lounge.personal

import android.os.Bundle

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.ActivityPersonalLoungeBinding


@AndroidEntryPoint
class PersonalLoungeActivity : AppCompatActivity() {

    // Fragment용 바인딩 클래스를 그대로 사용
    private lateinit var binding: ActivityPersonalLoungeBinding
    private val viewModel: SharedCollectionsViewModel by viewModels()
    private var friendId : Int = 0
    private var friendNickname  = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalLoungeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        friendId  = intent.getIntExtra("FRIEND_ID", 0)
        friendNickname = intent.getStringExtra("friend_nickname").toString()
        viewModel.getFriendPublicCollections(friendId)


        val bundle = Bundle()
        bundle.putString("friend_nickname", friendNickname)

        val fragment = PersonalLoungeFragment()
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.personal_lounge_content_container, fragment )
            .commit()


        binding.tvFriendNickname.text = "[${friendNickname}]'s Lounge"


        binding.btnExit.setOnClickListener {
            finish()
        }
    }


}