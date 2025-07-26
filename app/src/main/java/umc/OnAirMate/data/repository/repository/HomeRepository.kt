package umc.OnAirMate.data.repository.repository

import retrofit2.Response
import umc.OnAirMate.data.model.DefaultResponse
import umc.OnAirMate.data.model.entity.RoomData
import umc.OnAirMate.data.model.request.CreateRoomRequest
import umc.OnAirMate.data.model.response.CreateRoomResponse
import umc.OnAirMate.data.model.response.RoomListResponse

interface HomeRepository {
    suspend fun getRoomList( accessToken: String): Response<DefaultResponse<RoomListResponse>>
    suspend fun getRoomInfo( accessToken: String, roomId : Int) : Response<DefaultResponse<RoomData>>
    suspend fun createRoom( accessToken: String, body : CreateRoomRequest): Response<DefaultResponse<CreateRoomResponse>>
}