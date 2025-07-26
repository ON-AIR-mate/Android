package umc.onairmate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import umc.onairmate.databinding.ActivityMainBinding
import umc.onairmate.ui.friend.FriendFragment
import umc.onairmate.ui.home.HomeFragment
import umc.onairmate.ui.lounge.LoungeFragment
import umc.onairmate.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 앱 처음 시작시 HomeFragment 표시
        openFragment(HomeFragment())

        // BottomNavigation 클릭 시 Fragment 변경
        binding.navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    openFragment(HomeFragment())
                    true
                }
                R.id.navigation_lounge -> {
                    openFragment(LoungeFragment())
                    true
                }
                R.id.navigation_friend -> {
                    openFragment(FriendFragment())
                    true
                }
                R.id.navigation_profile -> {
                    openFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentContainer, fragment)
            .commit()
    }
}
