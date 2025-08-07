package umc.onairmate.data.repository.repository

import umc.onairmate.data.api.TestService
import umc.onairmate.data.model.entity.LoginData
import umc.onairmate.data.model.request.TestRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.LoginResponse
import umc.onairmate.data.model.response.MessageResponse
import javax.inject.Inject

interface TestRepository {
    suspend fun signUp(body: TestRequest): DefaultResponse<MessageResponse>
    suspend fun login(body: LoginData): DefaultResponse<LoginResponse>
}