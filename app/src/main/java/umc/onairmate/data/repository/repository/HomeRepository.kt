package umc.onairmate.data.repository.repository

import retrofit2.Response
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.response.CreateRoomResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.RoomListResponse

interface HomeRepository {
    suspend fun getRoomList( accessToken: String): Response<DefaultResponse<RoomListResponse>>
    suspend fun getRoomInfo( accessToken: String, roomId : Int) : Response<DefaultResponse<RoomData>>
    suspend fun createRoom( accessToken: String, body : CreateRoomRequest): Response<DefaultResponse<CreateRoomResponse>>
}