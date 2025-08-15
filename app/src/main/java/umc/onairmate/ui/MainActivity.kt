package umc.onairmate.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.OnAirMateApplication
import umc.onairmate.R
import umc.onairmate.data.socket.SocketDispatcher
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val navGraphIds = listOf(
            R.navigation.nav_home,
            R.navigation.nav_lounge,
            R.navigation.nav_friend,
            R.navigation.nav_profile
        )
        

        binding.navView.setupWithNavController(navController)

        // 2) 현재 목적지와 BottomNav 동기화 (혹시라도 틀어지면 잡아줌)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val id = destination.id
            Log.d("BNV", "destination=${resources.getResourceEntryName(id)}")
            when (id) {
                R.id.navigation_home,
                R.id.navigation_lounge,
                R.id.navigation_friend,
                R.id.navigation_profile -> {
                    if (binding.navView.selectedItemId != id) {
                        binding.navView.selectedItemId = id
                    }
                }
                else -> { /* 서브화면이면 탭 상태 유지 */ }
            }
        }

        binding.navView.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.popBackStack(R.id.navigation_home, false)
                }
                R.id.navigation_lounge -> {
                    navController.popBackStack(R.id.navigation_lounge, false)
                }
                R.id.navigation_friend -> {
                    navController.popBackStack(R.id.navigation_friend, false)
                }
                R.id.navigation_profile -> {
                    navController.popBackStack(R.id.navigation_profile, false)
                }
            }
        }

        navView.setupWithNavController(navController)

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