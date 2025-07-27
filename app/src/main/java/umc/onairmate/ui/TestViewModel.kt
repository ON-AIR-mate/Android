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
import umc.onairmate.data.repository.repository.TestRepository
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val repository: TestRepository,
    @ApplicationContext private val context: Context
) : ViewModel(){

    private val TAG = this.javaClass.simpleName
    private val spf = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)


    fun signUp(num : Int){
        viewModelScope.launch {
            try{
                val body = TestRequest(username = "qwer${num}", password = "!Qwer111${num}", nickname = "user${num}","1", TestRequest.Agreement())
                val response  = repository.signUp(body)
                if(response.success){
                    Log.d(TAG, "signUp 응답 성공: ${response}")

                }
                else Log.d(TAG, "signUp 응답 실패: ${response}")
            }catch (e: Exception){
                Log.d(TAG, "signUp api 요청 실패: ${e}")
            }
        }
    }

    fun login(num : Int){
        viewModelScope.launch {
            try{
                val body = LoginData(username = "qwer${num}", password = "!Qwer111${num}")
                val response  = repository.login(body)
                if(response.success){
                    Log.d(TAG, "signUp 응답 성공: ${response}")
                    spf.edit().putString("access_token",response.data!!.accessToken).commit()
                }
                else Log.d(TAG, "signUp 응답 실패: ${response.error}")
            }catch (e: Exception){
                Log.d(TAG, "signUp api 요청 실패: ${e}")
            }
        }
    }

}