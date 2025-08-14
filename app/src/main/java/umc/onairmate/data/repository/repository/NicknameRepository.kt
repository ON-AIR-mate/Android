package umc.onairmate.data.repository

interface NicknameRepository {
    suspend fun isNicknameDuplicated(nickname: String): Boolean
}