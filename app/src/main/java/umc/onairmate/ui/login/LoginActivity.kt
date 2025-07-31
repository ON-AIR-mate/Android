package umc.onairmate.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.ui.join.JoinProfileFragment
import umc.onairmate.R
import umc.onairmate.databinding.ActivityLoginBinding
import umc.onairmate.ui.MainActivity
import umc.onairmate.ui.MainActivity_GeneratedInjector
import umc.onairmate.ui.TestViewModel
import umc.onairmate.ui.home.SearchRoomViewModel
import kotlin.getValue

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: TestViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newprofile.setOnClickListener{
            //Toast.makeText(this,"회원가입 클릭", Toast.LENGTH_SHORT).show()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, JoinProfileFragment())
                .addToBackStack(null)
                .commit()
            /*
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
             */
        }
        binding.findid.setOnClickListener {
            val id = binding.etId.text.toString()
            viewModel.signUp(id)
        }

        binding.btnLogin.setOnClickListener {
            val id = binding.etId.text.toString()
            viewModel.login(id)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }
}