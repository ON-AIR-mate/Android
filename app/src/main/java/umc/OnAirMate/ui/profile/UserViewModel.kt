package umc.onairmate.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.entity.ParticipatedRoomData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.repository.repository.UserRepository
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val TAG = this.javaClass.simpleName

    // 토큰
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _rooms = MutableLiveData<List<ParticipatedRoomData>>()
    val rooms: LiveData<List<ParticipatedRoomData>> get() = _rooms

    //UX향상을 위해 서버가 로딩중임을 프로그래스바로 표시할 때 사용예정
    //api호출시 true, 응답이 오면 false로 한다 정도로만 생각
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // 삭제 요청의 진행 상태(UiState)를 저장하고 외부에서 관찰할 수 있도록 제공
    private val _deleteInfo = MutableLiveData<String>()
    val deleteInfo : LiveData<String> get() = _deleteInfo

    private fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun loadParticipatedRooms() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.getParticipatedRoom(token)
            Log.d(TAG, "getParticipatedRoom api 호출")

            when (result) {
                is DefaultResponse.Success<List<ParticipatedRoomData>> -> {
                    Log.d(TAG, "${result.data}")
                    _rooms.postValue(result.data.sortedByDescending { it.participatedAt })
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }

    }

    fun deleteParticipatedRoom(roomId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.deleteParticipatedRoom(token, roomId)
            Log.d(TAG, "deleteParticipatedRoom api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "${result.data}")
                    _deleteInfo.postValue(result.data.message)
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "${result.code} - ${result.message}")
                    _deleteInfo.postValue(result.message)
                }
            }
        }
    }
}