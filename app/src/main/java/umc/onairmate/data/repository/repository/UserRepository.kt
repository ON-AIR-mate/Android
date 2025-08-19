package umc.onairmate.data.repository.repository

import umc.onairmate.data.model.entity.ParticipatedRoomData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse

interface UserRepository {

    suspend fun getParticipatedRoom(
        accessToken: String
    ): DefaultResponse<List<ParticipatedRoomData>>

    suspend fun deleteParticipatedRoom(
        accessToken: String, // "Bearer xxx"
        roomId: Int
    ): DefaultResponse<MessageResponse>
}