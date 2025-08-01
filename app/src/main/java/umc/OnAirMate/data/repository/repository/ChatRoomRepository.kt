package umc.onairmate.data.repository.repository

import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse

interface ChatRoomRepository {

    // 방 설정 조회
    suspend fun getRoomSetting(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<RoomSettingData>

    // 방 설정 수정
    suspend fun setRoomSetting(
        accessToken: String,
        roomId: Int,
        body: RoomSettingData
    ): DefaultResponse<MessageResponse>

    // 방 참여자 목록 조회
    suspend fun getParticipantList(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<List<ParticipantData>>
}