package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.ChatRoomService
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.data.model.request.SummaryCreateRequest
import umc.onairmate.data.model.request.SummaryFeedbackRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.model.response.SummaryCreateResponse
import umc.onairmate.data.repository.repository.ChatRoomRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class ChatRoomRepositoryImpl @Inject constructor(
    private val api: ChatRoomService
): ChatRoomRepository {

    // 방 설정 조회
    override suspend fun getRoomSetting(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<RoomSettingData> {
        return safeApiCall {
            api.getRoomSetting(accessToken, roomId)
        }
    }

    // 방 설정 수정
    override suspend fun setRoomSetting(
        accessToken: String,
        roomId: Int,
        body: RoomSettingData
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.setRoomSetting(accessToken, roomId, body)
        }
    }

    // 방 참여자 목록 조회
    override suspend fun getParticipantList(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<List<ParticipantData>> {
        return safeApiCall {
            api.getParticipants(accessToken, roomId)
        }
    }

    // 채팅 로그 조회
    override suspend fun getChatHistory(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<List<ChatMessageData>> {
        return safeApiCall {
            api.getChatHistory(accessToken, roomId)
        }
    }

    // 방 종료시 채팅 요약 생성
    override suspend fun createChatSummary(
        accessToken: String,
        body: SummaryCreateRequest
    ): DefaultResponse<SummaryCreateResponse> {
        return safeApiCall {
            api.createChatSummary(accessToken, body)
        }
    }

    // 요약에 대한 피드백 제출
    override suspend fun sendFeedbackForSummary(
        accessToken: String,
        summaryId: String,
        body: SummaryFeedbackRequest
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.sendFeedbackForSummary(accessToken, summaryId, body)
        }
    }

}