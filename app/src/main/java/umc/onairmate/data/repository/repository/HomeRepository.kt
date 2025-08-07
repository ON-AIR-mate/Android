package umc.onairmate.data.repository.repository

import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.response.CreateRoomResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RoomListResponse

interface HomeRepository {
    // 방 검색
    suspend fun getRoomList( accessToken: String, sortBy : String, searchType : String, keyword : String): DefaultResponse<RoomListResponse>
    suspend fun getRoomDetailInfo( accessToken: String, roomId : Int) : DefaultResponse<RoomData>
    suspend fun createRoom( accessToken: String, body : CreateRoomRequest): DefaultResponse<CreateRoomResponse>
    suspend fun joinRoom( accessToken: String, roomId : Int): DefaultResponse<String>
    suspend fun leaveRoom(accessToken: String, roomId : Int): DefaultResponse<String>

    // 영상 검색
    suspend fun searchVideoList(accessToken: String, query: String, limit: Int): DefaultResponse<List<VideoData>>
    suspend fun getVideoDetailInfo(accessToken: String, videoId: String): DefaultResponse<VideoData>
    suspend fun getRecommendVideoList(accessToken: String, keyword: String, limit: Int): DefaultResponse<List<VideoData>>
}