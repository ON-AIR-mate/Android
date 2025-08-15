package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.BookmarkService
import umc.onairmate.data.model.request.BookmarkCreateRequest
import umc.onairmate.data.model.request.RoomWithBookmarkCreateRequest
import umc.onairmate.data.model.response.BookmarkListResponse
import umc.onairmate.data.model.response.BookmarkCreateResponse
import umc.onairmate.data.model.response.RoomWithBookmarkCreateResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.request.CollectionMoveRequest
import umc.onairmate.data.repository.repository.BookmarkRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val api: BookmarkService
): BookmarkRepository {

    // 북마크 생성
    override suspend fun createBookmark(
        accessToken: String,
        body: BookmarkCreateRequest
    ): DefaultResponse<BookmarkCreateResponse> {
        return safeApiCall {
            api.createBookmark(accessToken, body)
        }
    }

    // 북마크 목록 조회
    override suspend fun getBookmarks(
        accessToken: String,
        collectionId: Int?,
        uncategorized: Boolean
    ): DefaultResponse<BookmarkListResponse> {
        return safeApiCall {
            api.getBookmarks(accessToken, collectionId, uncategorized)
        }
    }

    // 북마크 삭제
    override suspend fun deleteBookmark(
        accessToken: String,
        bookmarkId: Int
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.deleteBookmark(accessToken, bookmarkId)
        }
    }

    // 북마크 컬렉션 이동
    override suspend fun moveCollectionOfBookmark(
        accessToken: String,
        bookmarkId: Int,
        body: CollectionMoveRequest
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.moveCollectionOfBookmark(accessToken, bookmarkId, body)
        }
    }

    // 북마크로 방 생성
    override suspend fun createRoomWithBookmark(
        accessToken: String,
        bookmarkId: Int,
        body: RoomWithBookmarkCreateRequest
    ): DefaultResponse<RoomWithBookmarkCreateResponse> {
        return safeApiCall {
            api.createRoomWithBookmark(accessToken, bookmarkId, body)
        }
    }
}