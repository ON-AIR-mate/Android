package umc.onairmate.ui.lounge.bookmark

import umc.onairmate.data.model.entity.RoomArchiveData

interface BookmarkEventListener {
    fun createRoomWithBookmark(roomArchiveData: RoomArchiveData) {}

    fun deleteBookmark(roomArchiveData: RoomArchiveData) {}

    fun moveCollection(roomArchiveData: RoomArchiveData) {}
}