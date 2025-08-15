package umc.onairmate.ui.friend.chat

import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.RoomData

interface FriendChatEventListener {
    fun collectionClick(data: CollectionData)
    fun invite(data: RoomData)
}