package umc.onairmate.data.repository.repository

import retrofit2.http.GET
import retrofit2.http.Header
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.ParticipatedRoomData
import umc.onairmate.data.model.response.RawDefaultResponse

interface UserRepository {

    suspend fun getParticipatedRoom(
        accessToken: String
    ): DefaultResponse<List<ParticipatedRoomData>>

    suspend fun deleteParticipatedRoom(
        accessToken: String, // "Bearer xxx"
        roomId: Long
    ): DefaultResponse<MessageResponse>
}