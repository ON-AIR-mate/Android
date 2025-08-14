package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.UserService
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.ParticipatedRoomData
import umc.onairmate.data.repository.repository.UserRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserService
):UserRepository{
    override suspend fun getParticipatedRoom(accessToken: String): DefaultResponse<List<ParticipatedRoomData>> {
        return safeApiCall { api.getParticipatedRoom(accessToken) }
    }

    override suspend fun deleteParticipatedRoom(
        accessToken: String,
        roomId: Long
    ): DefaultResponse<MessageResponse> {
        return safeApiCall { api.deleteParticipatedRoom(accessToken,roomId) }
    }

}