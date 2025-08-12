package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.FriendService
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.RequestedFriendData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.data.model.request.AcceptFriendBody
import umc.onairmate.data.model.request.FriendRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.repository.repository.FriendRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val api : FriendService
) : FriendRepository{
    override suspend fun getFriendList(accessToken: String): DefaultResponse<List<FriendData>> {
        return safeApiCall { api.getFriendList(accessToken) }
    }

    override suspend fun requestFriend(
        accessToken: String,
        body: FriendRequest
    ): DefaultResponse<MessageResponse> {
         return safeApiCall { api.requestFriend(accessToken,body) }
    }

    override suspend fun getRequestedFriendList(accessToken: String): DefaultResponse<List<RequestedFriendData>> {
         return safeApiCall { api.getRequestedFriendList(accessToken)}
    }

    override suspend fun acceptFriend(
        accessToken: String,
        requestId: Int,
        body: AcceptFriendBody
    ): DefaultResponse<MessageResponse> {
         return safeApiCall { api.acceptFriend(accessToken,requestId,body) }
    }

    override suspend fun deleteFriend(
        accessToken: String,
        userId: Int
    ): DefaultResponse<MessageResponse> {
         return safeApiCall { api.deleteFriend(accessToken,userId) }
    }

    override suspend fun searchUser(
        accessToken: String,
        nickname: String
    ): DefaultResponse<List<UserData>> {
         return safeApiCall { api.searchUser(accessToken,nickname) }
    }

    override suspend fun inviteFriend(
        accessToken: String,
        friendId: Int
    ): DefaultResponse<MessageResponse> {
        return safeApiCall { api.inviteFriend(accessToken, friendId) }
    }

    override suspend fun getDmHistory(
        accessToken: String,
        friendId: Int
    ): DefaultResponse<List<String>> {
        return safeApiCall { api.getDmHistory(accessToken, friendId) }
    }
}