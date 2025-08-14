package umc.onairmate.data.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.RequestedFriendData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.data.model.request.AcceptFriendBody
import umc.onairmate.data.model.request.FriendInviteRequest
import umc.onairmate.data.model.request.FriendRequest
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface FriendService {
    @GET("friends")
    suspend fun getFriendList(
        @Header("Authorization") accessToken: String
    ): RawDefaultResponse<List<FriendData>>

    @POST("friends/request")
    suspend fun requestFriend(
        @Header("Authorization") accessToken: String,
        @Body body: FriendRequest
    ): RawDefaultResponse<MessageResponse>

    @GET("friends/requests")
    suspend fun getRequestedFriendList(
        @Header("Authorization") accessToken: String
    ): RawDefaultResponse<List<RequestedFriendData>>

    @PUT("friends/requests/{requestId}")
    suspend fun acceptFriend(
        @Header("Authorization") accessToken: String,
        @Path("requestId") requestId : Int,
        @Body body : AcceptFriendBody
    ): RawDefaultResponse<MessageResponse>

    @DELETE("friends/{userId}")
    suspend fun deleteFriend(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId : Int
    ): RawDefaultResponse<MessageResponse>

    @GET("friends/search")
    suspend fun searchUser(
        @Header("Authorization") accessToken: String,
        @Query("nickname") nickname : String
    ): RawDefaultResponse<List<UserData>>

    @POST("friends/{friendId}/invite")
    suspend fun inviteFriend(
        @Header("Authorization") accessToken: String,
        @Path("friendId") friendId: Int,
        @Body body: FriendInviteRequest
    ): RawDefaultResponse<MessageResponse>
}