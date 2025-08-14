package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.model.response.CheckNicknameResponse
import umc.onairmate.data.api.AuthService
import umc.onairmate.data.model.entity.LoginData
import umc.onairmate.data.model.request.TestRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.LoginResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.repository.repository.AuthRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api : AuthService
) : AuthRepository {
    override suspend fun signUp(body: TestRequest): DefaultResponse<MessageResponse> {
        return safeApiCall{api.signUp(body)}
    }

    override suspend fun login(body: LoginData): DefaultResponse<LoginResponse> {
        return safeApiCall{api.login(body)}
    }

    override suspend fun checkNickname(nickname: String):DefaultResponse<CheckNicknameResponse> {
        return safeApiCall{api.checkNickname(nickname)}
    }
}