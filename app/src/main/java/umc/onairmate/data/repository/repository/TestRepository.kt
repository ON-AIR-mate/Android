package umc.onairmate.data.repository.repository

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import umc.onairmate.data.model.entity.LoginData
import umc.onairmate.data.model.request.TestRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.LoginResponse
import umc.onairmate.data.model.response.SignUpResponse

interface TestRepository {
    suspend fun signUp( body : TestRequest ) : DefaultResponse<SignUpResponse>
    suspend fun login( body : LoginData) : DefaultResponse<LoginResponse>
}