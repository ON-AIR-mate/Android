package umc.onairmate.data.repository.repository

import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse

interface UserRepository {
    suspend fun deleteParticipatedRoom(
        accessToken: String, // "Bearer xxx"
        roomId: Long
    ): DefaultResponse<MessageResponse>
}