package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.ChatRoomService
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.repository.repository.ChatRoomRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class ChatRoomRepositoryImpl @Inject constructor(
    private val api: ChatRoomService
): ChatRoomRepository {

    override suspend fun getRoomSetting(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<RoomSettingData> {
        return safeApiCall {
            api.getRoomSetting(accessToken, roomId)
        }
    }

    override suspend fun setRoomSetting(
        accessToken: String,
        roomId: Int,
        body: RoomSettingData
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.setRoomSetting(accessToken, roomId, body)
        }
    }

    override suspend fun getParticipantList(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<List<ParticipantData>> {
        return safeApiCall {
            api.getParticipants(accessToken, roomId)
        }
    }

}