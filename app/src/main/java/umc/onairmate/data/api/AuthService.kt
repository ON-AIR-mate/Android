package umc.onairmate.data.api


import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import umc.onairmate.data.model.response.CheckNicknameResponse
import umc.onairmate.data.model.entity.LoginData
import umc.onairmate.data.model.request.TestRequest

import umc.onairmate.data.model.response.LoginResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface AuthService {
    @POST("auth/register")
    suspend fun signUp(
        @Body body : TestRequest
    ) : RawDefaultResponse<MessageResponse>

    @POST("auth/login")
    suspend fun login(
        @Body body : LoginData
    ) : RawDefaultResponse<LoginResponse>

    @GET("auth/check-nickname/{nickname}")
    suspend fun checkNickname(
        @Path ("nickname") nickname : String
    ) : RawDefaultResponse<CheckNicknameResponse>
}