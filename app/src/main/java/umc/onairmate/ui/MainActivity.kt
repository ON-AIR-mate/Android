package umc.onairmate.ui

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_lounge,
                R.id.navigation_friend,
                R.id.navigation_profile
            )
        )

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