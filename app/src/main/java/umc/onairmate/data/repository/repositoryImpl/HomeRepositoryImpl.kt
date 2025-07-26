package umc.onairmate.data.repository.repositoryImpl

import retrofit2.Response
import umc.onairmate.data.api.HomeService
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.response.CreateRoomResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.RoomListResponse
import umc.onairmate.data.repository.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl  @Inject constructor(
    val api : HomeService
): HomeRepository {
    override suspend fun getRoomList(accessToken: String): Response<DefaultResponse<RoomListResponse>> {
        return api.getRoomList(accessToken)
    }

    override suspend fun getRoomInfo(
        accessToken: String,
        roomId: Int
    ): Response<DefaultResponse<RoomData>> {
        return api.getRoomInfo(accessToken, roomId)
    }

    override suspend fun createRoom(
        accessToken: String,
        body: CreateRoomRequest
    ): Response<DefaultResponse<CreateRoomResponse>> {
        return api.createRoom(accessToken, body)
    }

}