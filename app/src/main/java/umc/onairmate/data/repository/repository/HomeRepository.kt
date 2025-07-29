package umc.onairmate.data.repository.repository

import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.response.CreateRoomResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RoomListResponse

interface HomeRepository {
    suspend fun getRoomList( accessToken: String, sortBy : String, searchType : String, keyword : String): DefaultResponse<RoomListResponse>
    suspend fun getRoomInfo( accessToken: String, roomId : Int) : DefaultResponse<RoomData>
    suspend fun createRoom( accessToken: String, body : CreateRoomRequest): DefaultResponse<CreateRoomResponse>
    suspend fun joinRoom( accessToken: String, roomId : Int): DefaultResponse<MessageResponse>
    suspend fun leaveRoom(accessToken: String, roomId : Int): DefaultResponse<MessageResponse>

}