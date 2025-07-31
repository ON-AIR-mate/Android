package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.TestService
import umc.onairmate.data.model.entity.LoginData
import umc.onairmate.data.model.request.TestRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.LoginResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.repository.repository.TestRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val api : TestService
) : TestRepository {
    override suspend fun signUp(body: TestRequest): DefaultResponse<MessageResponse> {
        return safeApiCall{api.signUp(body)}
    }

    override suspend fun login(body: LoginData): DefaultResponse<LoginResponse> {
        return safeApiCall{api.login(body)}
    }
}