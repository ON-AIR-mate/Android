package umc.onairmate.data.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import umc.onairmate.data.model.request.CreateBookmarkRequest
import umc.onairmate.data.model.request.CreateRoomWithBookmarkRequest
import umc.onairmate.data.model.response.BookmarkListResponse
import umc.onairmate.data.model.response.CreateBookmarkResponse
import umc.onairmate.data.model.response.CreateRoomWithBookmarkResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.request.MoveCollectionRequest
import umc.onairmate.data.model.response.RawDefaultResponse

interface BookmarkService {

    // 북마크 생성
    @POST("bookmarks")
    suspend fun createBookmark(
        @Header("Authorization") accessToken: String,
        @Body body: CreateBookmarkRequest
    ): RawDefaultResponse<CreateBookmarkResponse>

    // 북마크 목록 조회
    @GET("bookmarks")
    suspend fun getBookmarks(
        @Header("Authorization") accessToken: String,
        @Query("collectionId") collectionId: Int?,
        @Query("uncategorized") uncategorized: Boolean
    ): RawDefaultResponse<BookmarkListResponse>

    // 북마크 삭제
    @DELETE("bookmarks/{bookmarkId}")
    suspend fun deleteBookmark(
        @Header("Authorization") accessToken: String,
        @Path("bookmarkId") bookmarkId: Int
    ): RawDefaultResponse<MessageResponse>

    // 북마크 컬렉션 이동
    @PUT("bookmarks/{bookmarkId}/collection")
    suspend fun moveCollectionOfBookmark(
        @Header("Authorization") accessToken: String,
        @Path("bookmarkId") bookmarkId: Int,
        @Body body: MoveCollectionRequest
    ): RawDefaultResponse<MessageResponse>

    // 북마크로 방 생성
    @POST("bookmarks/{bookmarkId}/create-room")
    suspend fun createRoomWithBookmark(
        @Header("Authorization") accessToken: String,
        @Path("bookmarkId") bookmarkId: Int,
        @Body body: CreateRoomWithBookmarkRequest
    ): RawDefaultResponse<CreateRoomWithBookmarkResponse>
}