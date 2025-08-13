package umc.onairmate.data.repository.repository

import retrofit2.Response
import umc.onairmate.data.model.request.JoinProfileRequest
import umc.onairmate.data.model.response.JoinProfileResponse
import umc.onairmate.data.model.response.NicknameCheckResponse

interface JoinRepository {
    suspend fun joinProfile(request: JoinProfileRequest): Response<JoinProfileResponse>
    suspend fun checkNickname(nickname: String): Response<NicknameCheckResponse>
}
