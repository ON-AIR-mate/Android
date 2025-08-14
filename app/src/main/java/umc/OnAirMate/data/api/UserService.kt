package umc.onairmate.data.api

import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.Path
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface UserService {
    @DELETE("users/participated-rooms/{roomId}")
    suspend fun deleteParticipatedRoom(
        @Header("Authorization") accessToken: String, // "Bearer xxx"
        @Path("roomId") roomId: Long
    ): RawDefaultResponse<MessageResponse>

}