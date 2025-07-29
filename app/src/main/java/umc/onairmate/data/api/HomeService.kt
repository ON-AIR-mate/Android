package umc.onairmate.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.response.RoomListResponse
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.response.CreateRoomResponse
import umc.onairmate.data.model.response.DefaultResponse

interface HomeService {
    @GET("rooms")
    suspend fun getRoomList(
        @Header("Authorization") accessToken: String
    ): DefaultResponse<RoomListResponse>

    @GET("rooms/{roomId}")
    suspend fun getRoomInfo(
        @Header("Authorization") accessToken: String,
        @Path("roomId") roomId : Int
    ) : DefaultResponse<RoomData>

    @POST("rooms")
    suspend fun createRoom(
        @Header("Authorization") accessToken: String,
        @Body body : CreateRoomRequest
    ): DefaultResponse<CreateRoomResponse>
}