package umc.onairmate.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import umc.onairmate.ui.nav.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.OnAirMateApplication
import umc.onairmate.R
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.databinding.ActivityMainBinding
import umc.onairmate.ui.home.HomeViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null
    private val navViewModel: NavViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val navGraphIds = listOf(
            R.navigation.nav_home,
            R.navigation.nav_lounge,
            R.navigation.nav_friend,
            R.navigation.nav_profile
        )
        // 멀티 백스택 연결

        binding.navView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment_activity_main,
            intent = intent,
            onTabSelected = { menuId, _ ->
                navViewModel.emitReset(menuId)      // 탭 전환 시
            },
            onTabReselected = { menuId, _ ->
                // 재선택에도 다시 로드하려면 동일 처리
                when (menuId) {
                    R.id.navigation_home   -> {}
                    R.id.navigation_lounge -> {}
                    R.id.navigation_friend ->{}
                    R.id.navigation_profile-> {}
                }
            }
        )

        connectSocket()
    }

    private fun connectSocket(){
        val spf = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = spf.getString("socket_token", null)


        if (!token.isNullOrBlank()) {
            // 소켓 초기화 및 연결
            SocketManager.init(OnAirMateApplication.getString(R.string.socket_url), token)
            SocketManager.connect()
        }
    }

}