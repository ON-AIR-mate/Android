package umc.onairmate.ui.lounge.personal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.R
import umc.onairmate.databinding.ActivityMainBinding // 일반적인 메인 액티비티 레이아웃 가정
import androidx.fragment.app.commit

@AndroidEntryPoint
class PersonalLoungeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Activity의 유일한 역할: PersonalLoungeFragment를 호스팅
        if (supportFragmentManager.findFragmentById(R.id.fragment_container) == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, PersonalLoungeFragment())
            }
        }
    }
}