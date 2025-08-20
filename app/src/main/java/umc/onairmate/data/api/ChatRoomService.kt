package umc.onairmate.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.data.model.request.SummaryCreateRequest
import umc.onairmate.data.model.request.SummaryFeedbackRequest
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.model.response.SummaryCreateResponse

interface ChatRoomService {

    // 방 설정 조회
    @GET("rooms/{roomId}/settings")
    suspend fun getRoomSetting(
        @Header("Authorization") accessToken: String,
        @Path("roomId") roomId: Int,
    ): RawDefaultResponse<RoomSettingData>

    // 방 설정 수정
    @PUT("rooms/{roomId}/settings")
    suspend fun setRoomSetting(
        @Header("Authorization") accessToken: String,
        @Path("roomId") roomId: Int,
        @Body body: RoomSettingData
    ): RawDefaultResponse<MessageResponse>

    // 방 참여자 목록 조회
    @GET("rooms/{roomId}/participants")
    suspend fun getParticipants(
        @Header("Authorization") accessToken: String,
        @Path("roomId") roomId: Int,
    ): RawDefaultResponse<List<ParticipantData>>

    // 채팅 로그 조회
    @GET("rooms/{roomId}/messages")
    suspend fun getChatHistory(
        @Header("Authorization") accessToken: String,
        @Path("roomId") roomId: Int,
    ): RawDefaultResponse<List<ChatMessageData>>

    // 방 종료시 채팅 요약 생성
    @POST("ai/summary")
    suspend fun createChatSummary(
        @Header("Authorization") accessToken: String,
        @Body body: SummaryCreateRequest
    ): RawDefaultResponse<SummaryCreateResponse>

    // 요약에 대한 피드백 제출
    @POST("ai/summary/{summaryId}/feedback")
    suspend fun sendFeedbackForSummary(
        @Header("Authorization") accessToken: String,
        @Path("summaryId") summaryId: String,
        @Body body: SummaryFeedbackRequest
    ): RawDefaultResponse<MessageResponse>
}