package umc.onairmate.data.api

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import umc.onairmate.data.model.entity.ParticipatedRoomData
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface UserService {
    @GET("users/participated-rooms")
    suspend fun getParticipatedRoom(
        @Header("Authorization") accessToken: String
    ): RawDefaultResponse<List<ParticipatedRoomData>>

    @DELETE("users/participated-rooms/{roomId}")
    suspend fun deleteParticipatedRoom(
        @Header("Authorization") accessToken: String, // "Bearer xxx"
        @Path("roomId") roomId: Int
    ): RawDefaultResponse<MessageResponse>

}