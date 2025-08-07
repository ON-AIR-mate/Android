package umc.onairmate.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import umc.onairmate.data.model.request.JoinProfileRequest
import umc.onairmate.data.model.response.JoinProfileResponse
import umc.onairmate.data.model.response.NicknameCheckResponse

interface JoinService {
    @POST("auth/register")
    suspend fun joinProfile(
        @Body request: JoinProfileRequest
    ): Response<JoinProfileResponse>

    @GET("auth/check-nickname/{nickname}")
    suspend fun checkNickname(
        @Query("nickname") nickname: String
    ): Response<NicknameCheckResponse>
}