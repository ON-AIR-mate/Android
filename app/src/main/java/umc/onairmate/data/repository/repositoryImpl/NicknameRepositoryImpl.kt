package umc.onairmate.data.repository.repositoryImpl

import android.util.Log
import umc.onairmate.data.api.NicknameService
import umc.onairmate.data.repository.NicknameRepository
import javax.inject.Inject

class NicknameRepositoryImpl @Inject constructor(
    private val api: NicknameService
): NicknameRepository {

    override suspend fun isNicknameDuplicated(nickname: String): Boolean {
        return try {
            val response = api.checkNickname(nickname)
            // available == true 면 사용 가능한 닉네임, 중복 아님 → 따라서 중복 여부는 반대(!)
            !response.data.available
        } catch (e: Exception) {
            Log.e("NicknameRepository", "닉네임 중복 검사 실패", e)
            // 실패 시 기본값 false 또는 true 선택 가능 (보통 실패는 중복 아님 false로 처리)
            false
        }
    }
}