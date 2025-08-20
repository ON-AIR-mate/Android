package umc.onairmate.ui.home.notification

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.response.NotificationResponse
import umc.onairmate.data.model.response.DefaultResponse

import umc.onairmate.data.repository.repository.NotificationRepository
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val repository: NotificationRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val TAG = this.javaClass.simpleName
    private val spf = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _notificationList = MutableLiveData<NotificationResponse?>()
    val notificationList: LiveData<NotificationResponse?> = _notificationList

    private val _count = MutableLiveData<Int>()
    val count : LiveData<Int> get() = _count

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getToken(): String? {
        return spf.getString("access_token", null)
    }

    fun getNotificationList(){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.getNotificationList(token)
            Log.d(TAG, "getNotificationList api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _notificationList.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _notificationList.value = null
                }
            }
            _isLoading.value = false
        }
    }
    fun getUnreadCount(){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.getUnreadCount(token)
            Log.d(TAG, "getUnreadCount api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _count.postValue(result.data.unreadCount)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _count.value = 0
                }
            }
            _isLoading.value = false
        }
    }
    fun updateReadCount(){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.updateReadCount(token)
            Log.d(TAG, "updateReadCount api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                }
            }
            _isLoading.value = false
        }
    }
}