package umc.onairmate.data.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import umc.onairmate.data.model.response.NicknameResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface NicknameService {
    @PUT("users/profile")
    suspend fun updateUserProfile(
        @Header("Authorization") accessToken: String,
        @Body body: UpdateProfileRequest
    ): RawDefaultResponse<NicknameResponse>
}