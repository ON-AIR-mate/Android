package umc.onairmate.ui.lounge.bookmark

import umc.onairmate.data.model.entity.BookmarkData

interface BookmarkEventListener {
    fun createRoomWithBookmark(bookmark: BookmarkData) {}

    fun deleteBookmark(bookmark: BookmarkData) {}

    fun moveCollection(bookmark: BookmarkData) {}
}