package umc.onairmate.data.repository.repository

import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse

interface ChatRoomRepository {
    suspend fun setRoomSetting(
        accessToken: String,
        roomId: Int,
        body: RoomSettingData
    ): DefaultResponse<MessageResponse>

    suspend fun getParticipantList(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<List<ParticipantData>>

    suspend fun getChatHistory(
        accessToken: String,
        roomId: Int,
    ): DefaultResponse<List<ChatMessageData>>
}