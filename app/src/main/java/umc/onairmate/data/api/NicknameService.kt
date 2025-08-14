package umc.onairmate.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import umc.onairmate.data.model.response.NicknameResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface NicknameService {
    @GET("auth/check-nickname/{nickname}")
    suspend fun checkNickname(
        @Path("nickname") nickname: String
    ): NicknameResponse
}