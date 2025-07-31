package umc.onairmate.ui.chat_room

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.ParticipantResponse
import umc.onairmate.data.repository.repository.ChatRoomRepository
import javax.inject.Inject

@HiltViewModel
class ChatRoomViewModel @Inject constructor(
    private val repository: ChatRoomRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val TAG = this.javaClass.simpleName

    // 토큰
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // 채팅방 참가자 데이터
    private val _participantDataInfo = MutableLiveData<ParticipantResponse>()
    val participantDataInfo : LiveData<ParticipantResponse> get() = _participantDataInfo

    // 방 데이터 수정 여부 확인
    private val _isRoomSettingModifyDone = MutableLiveData<MessageResponse>()
    val isRoomSettingModifyDone : LiveData<MessageResponse> get() = _isRoomSettingModifyDone

    // 서버 로딩중 - 프로그래스바
    // api호출시 true, 응답이 오면 false
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun getParticipantDataInfo(roomId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.getParticipantList(token, roomId)
            Log.d(TAG, "getParticipantList api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d("응답 성공", "${result.data}")
                    _participantDataInfo.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d("에러", "${result.code} - ${result.message}")
                }
            }
            _isLoading.value = false
        }
    }

    fun setRoomSetting(roomId: Int, roomSetting: RoomSettingData) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.setRoomSetting(token, roomId, roomSetting)
            Log.d(TAG, "setRoomSetting api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d("응답 성공", "${result.data}")
                    _isRoomSettingModifyDone.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d("에러", "${result.code} - ${result.message}")
                }
            }
        }
    }
}