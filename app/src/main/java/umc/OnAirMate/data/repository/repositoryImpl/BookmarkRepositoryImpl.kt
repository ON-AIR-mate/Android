package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.BookmarkService
import umc.onairmate.data.model.request.CreateBookmarkRequest
import umc.onairmate.data.model.request.CreateRoomWithBookmarkRequest
import umc.onairmate.data.model.response.BookmarkListResponse
import umc.onairmate.data.model.response.CreateBookmarkResponse
import umc.onairmate.data.model.response.CreateRoomWithBookmarkResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.MoveCollectionResponse
import umc.onairmate.data.repository.repository.BookmarkRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val api: BookmarkService
): BookmarkRepository {
    override suspend fun createBookmark(
        accessToken: String,
        body: CreateBookmarkRequest
    ): DefaultResponse<CreateBookmarkResponse> {
        return safeApiCall {
            api.createBookmark(accessToken, body)
        }
    }

    override suspend fun getBookmarks(
        accessToken: String,
        collectionId: Int,
        uncategorized: Boolean
    ): DefaultResponse<BookmarkListResponse> {
        return safeApiCall {
            api.getBookmarks(accessToken, collectionId, uncategorized)
        }
    }

    override suspend fun deleteBookmark(
        accessToken: String,
        bookmarkId: Int
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.deleteBookmark(accessToken, bookmarkId)
        }
    }

    override suspend fun moveCollectionOfBookmark(
        accessToken: String,
        bookmarkId: Int,
        body: MoveCollectionResponse
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.moveCollectionOfBookmark(accessToken, bookmarkId, body)
        }
    }

    override suspend fun createRoomWithBookmark(
        accessToken: String,
        bookmarkId: Int,
        body: CreateRoomWithBookmarkRequest
    ): DefaultResponse<CreateRoomWithBookmarkResponse> {
        return safeApiCall {
            api.createRoomWithBookmark(accessToken, bookmarkId, body)
        }
    }
}