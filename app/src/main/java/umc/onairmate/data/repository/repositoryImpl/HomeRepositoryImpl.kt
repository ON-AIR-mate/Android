package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.HomeService
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.response.CreateRoomResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RoomListResponse
import umc.onairmate.data.repository.repository.HomeRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class HomeRepositoryImpl  @Inject constructor(
    private val api : HomeService
): HomeRepository {
    override suspend fun getRoomList(
        accessToken: String,
        sortBy: String,
        searchType: String,
        keyword: String
    ): DefaultResponse<RoomListResponse> {
       return safeApiCall{api.getRoomList(accessToken, sortBy, searchType, keyword)}
    }

    override suspend fun getRoomDetailInfo(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<RoomData> {
        return safeApiCall{api.getRoomDetailInfo(accessToken, roomId)}
    }

    override suspend fun createRoom(
        accessToken: String,
        body: CreateRoomRequest
    ): DefaultResponse<CreateRoomResponse> {
        return safeApiCall{api.createRoom(accessToken, body)}
    }

    override suspend fun joinRoom(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<MessageResponse> {
        return safeApiCall{api.joinRoom(accessToken, roomId)}
    }

    override suspend fun leaveRoom(
        accessToken: String,
        roomId: Int
    ): DefaultResponse<MessageResponse> {
        return safeApiCall{api.leaveRoom(accessToken, roomId)}
    }

    override suspend fun searchVideoList(
        accessToken: String,
        query: String,
        limit: Int
    ): DefaultResponse<List<VideoData>> {
        return safeApiCall {
            api.searchVideoList(accessToken, query, limit)
        }
    }

    override suspend fun getVideoDetailInfo(
        accessToken: String,
        videoId: String
    ): DefaultResponse<VideoData> {
        return safeApiCall {
            api.getVideoDetailInfo(accessToken, videoId)
        }
    }

    override suspend fun getRecommendVideoList(
        accessToken: String,
        keyword: String,
        limit: Int
    ): DefaultResponse<List<VideoData>> {
        return safeApiCall {
            api.getRecommendVideoList(accessToken, keyword, limit)
        }
    }

}