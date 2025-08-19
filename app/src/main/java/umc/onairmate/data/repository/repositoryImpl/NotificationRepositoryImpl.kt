package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.NotificationService
import umc.onairmate.data.model.response.NotificationResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.ReadCountResponse
import umc.onairmate.data.repository.repository.NotificationRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val api : NotificationService
) : NotificationRepository{
    override suspend fun getNotificationList(accessToken: String): DefaultResponse<NotificationResponse> {
       return safeApiCall { api.getNotificationList(accessToken) }
    }

    override suspend fun getUnreadCount(accessToken: String): DefaultResponse<ReadCountResponse> {
        return safeApiCall { api.getUnreadCount(accessToken) }
    }

    override suspend fun updateReadCount(accessToken: String): DefaultResponse<MessageResponse> {
        return safeApiCall { api.updateReadCount(accessToken) }
    }
}