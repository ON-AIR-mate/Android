package umc.onairmate.ui.chat_room.summary

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.request.SummaryCreateRequest
import umc.onairmate.data.model.request.SummaryFeedbackRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.SummaryCreateResponse
import umc.onairmate.data.repository.repository.ChatRoomRepository
import javax.inject.Inject

@HiltViewModel
class SummaryViewModel @Inject constructor(
    private val repository: ChatRoomRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val TAG = this.javaClass.simpleName

    // 토큰
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _createdSummaryDataInfo = MutableLiveData<SummaryCreateResponse>()
    val createdSummaryDataInfo : LiveData<SummaryCreateResponse> get() = _createdSummaryDataInfo

    private val _isFeedbackSent = MutableLiveData<MessageResponse>()
    val isFeedbackSent : LiveData<MessageResponse> get() = _isFeedbackSent

    // 서버 로딩중 - 프로그래스바
    // api호출시 true, 응답이 오면 false
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    // 방 종료시 채팅 요약 생성
    fun createChatSummary(body: SummaryCreateRequest) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            val result = repository.createChatSummary(token, body)
            Log.d(TAG, "createChatSummary api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: ${result.data}")
                    _createdSummaryDataInfo.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "에러: ${result.code} - ${result.message}")
                }
            }
        }
    }

    // 요약에 대한 피드백 제출
    fun sendFeedbackForSummary(summaryId: String, body: SummaryFeedbackRequest) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            val result = repository.sendFeedbackForSummary(token, summaryId, body)
            Log.d(TAG, "sendFeedbackForSummary api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답 성공: ${result.data}")
                    _isFeedbackSent.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "에러: ${result.code} - ${result.message}")
                }
            }
        }
    }
}