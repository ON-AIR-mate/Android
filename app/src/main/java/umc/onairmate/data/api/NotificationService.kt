package umc.onairmate.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import umc.onairmate.data.model.response.NotificationResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.model.response.ReadCountResponse

interface NotificationService {

    @GET("notifications")
    suspend fun getNotificationList(
        @Header("Authorization") accessToken: String
    ): RawDefaultResponse<NotificationResponse>

    @GET("notifications/unread-count")
    suspend  fun getUnreadCount(
        @Header("Authorization") accessToken: String
    ): RawDefaultResponse<ReadCountResponse>

    @PUT("notifications/read")
    suspend fun updateReadCount(
        @Header("Authorization") accessToken: String
    ): RawDefaultResponse<MessageResponse>
}