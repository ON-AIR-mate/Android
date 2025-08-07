package umc.onairmate.data.repository

import umc.onairmate.data.api.NicknameService
import javax.inject.Inject

class NicknameRepository @Inject constructor(
    private val nicknameService: NicknameService
) {
    suspend fun isNicknameDuplicated(nickname: String): Boolean {
        // API 호출 등 로직
    }
}
