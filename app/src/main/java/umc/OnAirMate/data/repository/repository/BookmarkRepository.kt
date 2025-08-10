package umc.onairmate.data.repository.repository

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
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.MoveCollectionResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface BookmarkRepository {

    // 북마크 생성
    suspend fun createBookmark(
        accessToken: String,
        body: CreateBookmarkRequest
    ): DefaultResponse<CreateBookmarkResponse>

    // 북마크 목록 조회
    suspend fun getBookmarks(
        accessToken: String,
        collectionId: Int,
        uncategorized: Boolean
    ): DefaultResponse<BookmarkListResponse>

    // 북마크 삭제
    suspend fun deleteBookmark(
        accessToken: String,
        bookmarkId: Int
    ): DefaultResponse<MessageResponse>

    // 북마크 컬렉션 이동
    suspend fun moveCollectionOfBookmark(
        accessToken: String,
        bookmarkId: Int,
        body: MoveCollectionResponse
    ): DefaultResponse<MessageResponse>

    // 북마크로 방 생성
    suspend fun createRoomWithBookmark(
        accessToken: String,
        bookmarkId: Int,
        body: CreateRoomWithBookmarkRequest
    ): DefaultResponse<CreateRoomWithBookmarkResponse>
}