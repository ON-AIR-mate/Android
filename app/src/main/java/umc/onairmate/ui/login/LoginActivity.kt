package umc.OnAirMate.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.onairmate.ui.join.JoinProfileFragment
import umc.onairmate.R
import umc.onairmate.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

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
    }
}