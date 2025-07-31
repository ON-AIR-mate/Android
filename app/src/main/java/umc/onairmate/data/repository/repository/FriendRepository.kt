package umc.onairmate.data.repository.repository


import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.RequestedFriendData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.DefaultResponse

interface FriendRepository {
    suspend fun getFriendList( accessToken: String): DefaultResponse<List<FriendData>>
    suspend fun requestFriend( accessToken: String, targetUserId : Int): DefaultResponse<MessageResponse>
    suspend fun getRequestedFriendList( accessToken: String): DefaultResponse<List<RequestedFriendData>>
    suspend fun acceptFriend( accessToken: String, requestId : Int, action : String): DefaultResponse<MessageResponse>
    suspend fun deleteFriend( accessToken: String, userId : Int): DefaultResponse<MessageResponse>
    suspend fun searchUser( accessToken: String, nickname : String): DefaultResponse<List<UserData>>
}