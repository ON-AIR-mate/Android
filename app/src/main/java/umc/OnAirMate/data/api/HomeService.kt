package umc.OnAirMate.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import umc.OnAirMate.data.model.entity.RoomData
import umc.OnAirMate.data.model.response.RoomListResponse
import umc.OnAirMate.data.model.request.CreateRoomRequest
import umc.OnAirMate.data.model.DefaultResponse
import umc.OnAirMate.data.model.response.CreateRoomResponse

interface HomeService {
    @GET("/rooms")
    suspend fun getRoomList(
        @Header("Authorization") accessToken: String
    ): Response<DefaultResponse<RoomListResponse>>

    @GET("/rooms/{roomId}")
    suspend fun getRoomInfo(
        @Header("Authorization") accessToken: String,
        @Path("roomId") roomId : Int
    ) : Response<DefaultResponse<RoomData>>

    @POST("/rooms")
    suspend fun createRoom(
        @Header("Authorization") accessToken: String,
        @Body body : CreateRoomRequest
    ): Response<DefaultResponse<CreateRoomResponse>>
}