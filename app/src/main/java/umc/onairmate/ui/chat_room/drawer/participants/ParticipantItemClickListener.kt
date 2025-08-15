package umc.onairmate.ui.chat_room.drawer.participants

import umc.onairmate.data.model.entity.ParticipantData

interface ParticipantItemClickListener {
    fun clickReport(data: ParticipantData)
    fun clickRecommend(data: ParticipantData)
    fun clickAddFriend(data: ParticipantData)
    fun clickBlock(data: ParticipantData)
}