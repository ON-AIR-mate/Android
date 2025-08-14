package umc.onairmate.data.repository.repository

import umc.onairmate.data.model.response.CheckNicknameResponse
import umc.onairmate.data.model.entity.LoginData
import umc.onairmate.data.model.request.TestRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.LoginResponse
import umc.onairmate.data.model.response.MessageResponse

interface AuthRepository {
    suspend fun signUp( body : TestRequest ) : DefaultResponse<MessageResponse>
    suspend fun login( body : LoginData) : DefaultResponse<LoginResponse>
    suspend fun checkNickname(nickname : String) : DefaultResponse<CheckNicknameResponse>
}