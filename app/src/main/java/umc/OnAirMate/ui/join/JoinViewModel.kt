package umc.onairmate.ui.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import umc.onairmate.data.model.request.JoinProfileRequest
import umc.onairmate.data.model.response.JoinProfileResponse
import umc.onairmate.data.model.response.NicknameCheckResponse
import umc.onairmate.data.repository.repository.JoinRepository
import javax.inject.Inject

@HiltViewModel
class JoinViewModel @Inject constructor(
    private val joinRepository: JoinRepository
) : ViewModel() {

    private val _joinResult = MutableLiveData<JoinProfileResponse>()
    val joinResult: LiveData<JoinProfileResponse> = _joinResult

    // 닉네임 중복 체크 결과: true면 사용 가능, false면 중복
    private val _nicknameCheckResult = MutableLiveData<Boolean>()
    val nicknameCheckResult: LiveData<Boolean> = _nicknameCheckResult

    fun joinProfile(request: JoinProfileRequest) {
        viewModelScope.launch {
            try {
                val response = joinRepository.joinProfile(request)
                _joinResult.value = response.body()
            } catch (e: Exception) {
                _joinResult.value = JoinProfileResponse(false, "회원가입 실패: ${e.message}")
            }
        }
    }

    fun checkNicknameDuplication(nickname: String) {
        viewModelScope.launch {
            try {
                val response: Response<NicknameCheckResponse> = joinRepository.checkNickname(nickname)
                if (response.isSuccessful) {
                    // API 명세에 따라 isAvailable 값이 true면 사용 가능
                    _nicknameCheckResult.value = response.body()?.isAvailable ?: false
                } else {
                    _nicknameCheckResult.value = false
                }
            } catch (e: Exception) {
                _nicknameCheckResult.value = false
            }
        }
    }
}