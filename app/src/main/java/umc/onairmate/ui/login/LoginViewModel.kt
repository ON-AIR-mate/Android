package umc.onairmate.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import umc.onairmate.data.model.entity.LoginData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.LoginResponse
import umc.onairmate.data.repository.repository.TestRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: TestRepository
) : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val response = loginRepository.login(LoginData(username, password))
                when (response) {
                    is DefaultResponse.Success -> {
                        _loginResult.value = Result.success(response.data)
                    }
                    is DefaultResponse.Error -> {
                        _loginResult.value = Result.failure(Exception(response.message ?: "로그인 실패"))
                    }
                }
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            }
        }
    }

}
