package umc.onairmate.data.repository.repositoryImpl

import retrofit2.Response
import umc.onairmate.data.api.JoinService
import umc.onairmate.data.model.request.JoinProfileRequest
import umc.onairmate.data.model.response.JoinProfileResponse
import umc.onairmate.data.model.response.NicknameCheckResponse
import umc.onairmate.data.repository.repository.JoinRepository
import javax.inject.Inject

class JoinRepositoryImpl @Inject constructor(
    private val joinService: JoinService
) : JoinRepository {
    override suspend fun joinProfile(request: JoinProfileRequest): Response<JoinProfileResponse> {
        return joinService.joinProfile(request)
    }

    override suspend fun checkNickname(nickname: String): Response<NicknameCheckResponse> {
        return joinService.checkNickname(nickname)
    }
}