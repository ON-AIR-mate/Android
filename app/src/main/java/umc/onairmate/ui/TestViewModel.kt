package umc.onairmate.ui

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.entity.LoginData
import umc.onairmate.data.model.request.TestRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.repository.repository.AuthRepository
import javax.inject.Inject
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import umc.onairmate.ui.util.SharedPrefUtil

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel(){

    private val TAG = this.javaClass.simpleName
    private val spf = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _available = MutableLiveData<Boolean>()
    val available: LiveData<Boolean> = _available

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message
    fun signUp(id: String, pw: String, nickname: String, profile: String, agreements: TestRequest.Agreement){
        viewModelScope.launch {
            val body = TestRequest(username = id, password = pw, nickname = nickname,profile, agreements)
            val result = repository.signUp(body)
            Log.d(TAG, "signUp api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _isSuccess.value = true
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _isSuccess.value = false
                }
            }
        }
    }

    fun login(id: String, pw: String){
        viewModelScope.launch {
            val body = LoginData(username = id, password = pw)
            val result = repository.login(body)
            Log.d(TAG, "login api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    spf.edit {
                        putString("access_token", "Bearer " + result.data.accessToken)
                        putString("socket_token",result.data.accessToken)

                        putString("nickname", result.data.user.nickname)
                        putInt("userId",result.data.user.userId)
                    }
                    SharedPrefUtil.saveData("user_info", result.data.user)
                    _isSuccess.value = true
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")

                }
            }
        }
    }

    fun checkNickname(nickname: String){
        viewModelScope.launch {
            val result = repository.checkNickname(nickname)
            Log.d(TAG, "checkNickname api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _available.value = result.data.available
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _available.value = false
                }
            }
        }
    }

}