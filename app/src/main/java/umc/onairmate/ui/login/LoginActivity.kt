package umc.onairmate.ui.login

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import umc.onairmate.databinding.ActivityLoginBinding
import kotlin.getValue

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val  loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.isLoading.observe(this){isLoading->
            binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }


    }


}