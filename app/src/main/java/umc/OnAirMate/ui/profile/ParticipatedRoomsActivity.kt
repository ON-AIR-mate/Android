package umc.onairmate.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import umc.onairmate.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ParticipatedRoomsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_participated_rooms)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(
                    R.id.fragment_container,
                    ParticipatedRoomsFragment()  // 목표 프래그먼트
                )
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, ParticipatedRoomsActivity::class.java)
    }
}
