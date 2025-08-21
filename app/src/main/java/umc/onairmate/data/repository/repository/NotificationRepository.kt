package umc.onairmate.data.repository.repository


import umc.onairmate.data.model.response.NotificationResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.ReadCountResponse

interface NotificationRepository {

    suspend fun getNotificationList(accessToken: String ): DefaultResponse<NotificationResponse>
    suspend fun getUnreadCount(accessToken: String): DefaultResponse<ReadCountResponse>
    suspend fun updateReadCount(accessToken: String): DefaultResponse<MessageResponse>
}