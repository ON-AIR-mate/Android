package umc.onairmate.ui.friend.list

import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.RequestedFriendData

interface FriendItemClickListener {
    fun clickMessage()
    fun acceptRequest(data: RequestedFriendData)

    // 새로 추가할 항목
    fun clickCollection(data: FriendData)
    fun clickDelete(data: FriendData)
    fun clickBlock(data: FriendData)
    fun clickReport(data: FriendData)
}