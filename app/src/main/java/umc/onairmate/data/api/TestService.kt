package umc.onairmate.data.api


import retrofit2.http.Body
import retrofit2.http.POST
import umc.onairmate.data.model.entity.LoginData
import umc.onairmate.data.model.request.TestRequest

import umc.onairmate.data.model.response.LoginResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface TestService {
    @POST("auth/register")
    suspend fun signUp(
        @Body body : TestRequest
    ) : RawDefaultResponse<MessageResponse>

    @POST("auth/login")
    suspend fun login(
        @Body body : LoginData
    ) : RawDefaultResponse<LoginResponse>
}