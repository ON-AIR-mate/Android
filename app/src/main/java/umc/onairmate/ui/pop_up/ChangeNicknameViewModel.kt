package umc.onairmate.ui.pop_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import umc.onairmate.data.repository.NicknameRepository

@HiltViewModel
class ChangeNicknameViewModel @Inject constructor(
    private val nicknameRepository: NicknameRepository
) : ViewModel() {

    // 닉네임 중복 확인 결과를 콜백으로 전달하거나 LiveData로 관리 가능
    fun checkNickname(
        nickname: String,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val isDuplicated = nicknameRepository.isNicknameDuplicated(nickname)
                onResult(isDuplicated)
            } catch (e: Exception) {
                // 에러 처리 필요하면 여기서
                onResult(false) // 중복 아님으로 처리하거나 별도 처리
            }
        }
    }
}