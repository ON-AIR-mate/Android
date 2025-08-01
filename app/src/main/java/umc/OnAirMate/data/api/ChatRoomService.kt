package umc.onairmate.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import umc.onairmate.data.model.entity.ParticipantData
import umc.onairmate.data.model.entity.RoomSettingData
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse

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

    // ai 요약이랑 북마크 생성도 여긴가?
}