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
import umc.onairmate.data.repository.repository.TestRepository
import javax.inject.Inject
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: TestRepository,
    @ApplicationContext private val context: Context
) : ViewModel(){

    private val TAG = this.javaClass.simpleName
    private val spf = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    fun signUp(id: String, pw: String){
        viewModelScope.launch {
            val body = TestRequest(username = id, password = pw, nickname = id,"1", TestRequest.Agreement())
            val result = repository.signUp(body)
            Log.d(TAG, "signUp api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    login(id,pw)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")

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
                    _isSuccess.postValue(true)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")

                }
            }
        }
    }

}