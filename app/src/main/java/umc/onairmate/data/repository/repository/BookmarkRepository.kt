package umc.onairmate.data.repository.repository

import umc.onairmate.data.model.request.BookmarkCreateRequest
import umc.onairmate.data.model.request.RoomWithBookmarkCreateRequest
import umc.onairmate.data.model.response.BookmarkListResponse
import umc.onairmate.data.model.response.BookmarkCreateResponse
import umc.onairmate.data.model.response.RoomWithBookmarkCreateResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.request.CollectionMoveRequest

interface BookmarkRepository {

    // 북마크 생성
    suspend fun createBookmark(
        accessToken: String,
        body: BookmarkCreateRequest
    ): DefaultResponse<BookmarkCreateResponse>

    // 북마크 목록 조회
    suspend fun getBookmarks(
        accessToken: String,
        collectionId: Int?,
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
        body: CollectionMoveRequest
    ): DefaultResponse<MessageResponse>

    // 북마크로 방 생성
    suspend fun createRoomWithBookmark(
        accessToken: String,
        bookmarkId: Int,
        body: RoomWithBookmarkCreateRequest
    ): DefaultResponse<RoomWithBookmarkCreateResponse>
}