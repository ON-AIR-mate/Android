package umc.onairmate.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.response.RoomListResponse
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.response.CreateRoomResponse
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.model.response.MessageResponse

interface HomeService {
    @GET("rooms")
    suspend fun getRoomList(
        @Header("Authorization") accessToken: String,
        @Query ("sortBy") sortBy : String,
        @Query ("searchType") searchType : String,
        @Query (value = "keyword") keyword : String
    ): RawDefaultResponse<RoomListResponse>

    @GET("rooms/{roomId}")
    suspend fun getRoomDetailInfo(
        @Header("Authorization") accessToken: String,
        @Path("roomId") roomId : Int
    ) : RawDefaultResponse<RoomData>

    @POST("rooms")
    suspend fun createRoom(
        @Header("Authorization") accessToken: String,
        @Body body : CreateRoomRequest
    ): RawDefaultResponse<CreateRoomResponse>

    @POST("rooms/{roomId}/join")
    suspend fun joinRoom(
        @Header("Authorization") accessToken: String,
        @Path("roomId") roomId : Int
    ): RawDefaultResponse<MessageResponse>

    @POST("rooms/{roomId}/leave")
    suspend fun leaveRoom(
        @Header("Authorization") accessToken: String,
        @Path("roomId") roomId : Int
    ): RawDefaultResponse<MessageResponse>

}