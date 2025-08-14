package umc.onairmate.ui.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import androidx.lifecycle.Observer
import android.widget.Toast
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.response.ParticipatedRoomData
import umc.onairmate.data.repository.repository.UserRepository
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import javax.inject.Inject
import androidx.lifecycle.map


@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    @ApplicationContext private val context: Context
) : ViewModel()
{


    private val TAG = this.javaClass.simpleName

    // 토큰
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)


    private val _rooms = MutableLiveData<List<ParticipatedRoomData>>(emptyList())
    val rooms: LiveData<List<ParticipatedRoomData>> get() = _rooms


    //UX향상을 위해 서버가 로딩중임을 프로그래스바로 표시할 때 사용예정
    //api호출시 true, 응답이 오면 false로 한다 정도로만 생각
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    // 삭제 요청의 진행 상태(UiState)를 저장하고 외부에서 관찰할 수 있도록 제공
    private val _deleteState = MutableLiveData<UiState>(UiState.Idle)
    val deleteState : LiveData<UiState> get() = _deleteState

    private fun parseCode(code: String?): Int = code?.toIntOrNull() ?: -1

    private fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun deleteParticipated(roomId: Long) {
        viewModelScope.launch {
            _deleteState.value = UiState.Loading

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
                    val msg = result.data?.message ?: "삭제 성공"
                    _deleteState.value = UiState.Success(msg)
                }
                is DefaultResponse.Error -> {
                    val msg = result.message ?: "에러"
                    _deleteState.value = UiState.Error(msg)
                }
            }
            _isLoading.value = false
        }
    }

    val deleteMessage: LiveData<String?> = deleteState.map { state ->
        when (state) {
            is UiState.Success -> state.msg
            is UiState.Error   -> state.msg
            else               -> null            // Loading/Idle은 메시지 없음
        }
    }

    fun loadParticipatedRooms() {
        viewModelScope.launch {
            val token = getToken() ?: return@launch
            _isLoading.value = true
            when (val res = repository.getParticipatedRoom(token)) {
                is DefaultResponse.Success -> _rooms.postValue(res.data)
                is DefaultResponse.Error -> handleError(res.code?.toIntOrNull() ?: -1, res.message)
            }
            _isLoading.value = false
        }

    }

    fun deleteParticipatedRoom(roomId: Long) {
        viewModelScope.launch {
            val token = getToken() ?: return@launch
            _deleteState.value = UiState.Loading

            when (val res = repository.deleteParticipatedRoom(token, roomId)) {
                is DefaultResponse.Success -> {
                    val msg = res.data?.message ?: "삭제 완료"
                    _deleteState.value = UiState.Success(msg)
                    // 로컬 리스트에서도 제거
                    _rooms.value = _rooms.value?.filterNot { it.roomId.toLong() == roomId }
                }
                is DefaultResponse.Error -> {
                    when (parseCode(res.code)) {
                        401 -> _deleteState.value = UiState.Error("로그인이 필요합니다.")
                        409 -> _deleteState.value = UiState.Error("진행중인 방은 삭제할 수 없습니다.")
                        404 -> {
                            _deleteState.value = UiState.Success("이미 삭제된 항목이어서 목록에서 제거했습니다.")
                            _rooms.value = _rooms.value?.filterNot { it.roomId.toLong() == roomId }
                        }
                        else -> _deleteState.value = UiState.Error("삭제 실패: ${res.message}")
                    }
                }
            }
        }
    }

    private fun handleError(code: Int, msg: String?) {
        // 필요 시 토스트/로그
        Log.e("UserViewModel", "loadParticipatedRooms error $code $msg")
    }
}


// 예시: UserViewModel.kt 혹은 RecentRoomsViewModel.kt
fun loadRecentParticipatedRooms() {
    viewModelScope.launch {
        val token = getToken() ?: return@launch
        _isLoading.value = true

        when (val res = repository.getParticipatedRooms(token)) {
            is DefaultResponse.Success -> {
                val mapped = res.data.map { room ->
                    ParticipatedRoomItem(
                        roomId = room.roomId.toLong(),
                        roomtitle = room.title,
                        videotitle = room.lastVideoTitle ?: room.title,  // 서버 필드에 맞춰 조정
                        bookmarktime = room.bookmarkTimeSec ?: 0,        // 서버 필드에 맞춰 조정
                        lastEnteredAt = room.lastEnteredAt               // ★ 정렬 기준
                    )
                }.sortedByDescending { it.lastEnteredAt.toMillisSafe() }   // ★ 내림차순

                _rooms.postValue(mapped) // LiveData<List<ParticipatedRoomItem>>
            }
            is DefaultResponse.Error -> handleError(res.code, res.message)
        }
        _isLoading.value = false
    }
}

// 편의 확장: String? → Long(에폭 ms)
private fun String?.toMillisSafe(): Long = try {
    if (this == null) 0L else try {
        java.time.Instant.parse(this).toEpochMilli()
    } catch (_: Exception) {
        val f = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        java.time.LocalDateTime.parse(this, f).atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
} catch (_: Exception) { 0L }



sealed interface UiState {
    data object Idle : UiState
    data object Loading : UiState
    data class Success(val msg: String) : UiState
    data class Error(val msg: String) : UiState
}