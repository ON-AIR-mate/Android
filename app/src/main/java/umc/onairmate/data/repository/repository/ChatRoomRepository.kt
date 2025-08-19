package umc.onairmate.data.repository.repository

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.data.model.request.SummaryCreateRequest
import umc.onairmate.data.model.request.SummaryFeedbackRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.model.response.SummaryCreateResponse

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

    suspend fun getChatHistory(
        accessToken: String,
        roomId: Int,
    ): DefaultResponse<List<ChatMessageData>>

    // 방 종료시 채팅 요약 생성
    suspend fun createChatSummary(
        accessToken: String,
        body: SummaryCreateRequest
    ): DefaultResponse<SummaryCreateResponse>

    // 요약에 대한 피드백 제출
    suspend fun sendFeedbackForSummary(
        accessToken: String,
        summaryId: String,
        body: SummaryFeedbackRequest
    ): DefaultResponse<MessageResponse>
}