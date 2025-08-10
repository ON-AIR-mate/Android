package umc.onairmate.data.repository.repository


import retrofit2.http.Header
import retrofit2.http.Path
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.RequestedFriendData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.data.model.request.AcceptFriendBody
import umc.onairmate.data.model.request.FriendRequest
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface FriendRepository {
    suspend fun getFriendList( accessToken: String): DefaultResponse<List<FriendData>>
    suspend fun requestFriend( accessToken: String, body: FriendRequest): DefaultResponse<MessageResponse>
    suspend fun getRequestedFriendList( accessToken: String): DefaultResponse<List<RequestedFriendData>>
    suspend fun acceptFriend( accessToken: String, requestId : Int, body : AcceptFriendBody): DefaultResponse<MessageResponse>
    suspend fun deleteFriend( accessToken: String, userId : Int): DefaultResponse<MessageResponse>
    suspend fun searchUser( accessToken: String, nickname : String): DefaultResponse<List<UserData>>
    suspend fun inviteFriend( accessToken: String, friendId: Int): DefaultResponse<MessageResponse>
}