package umc.onairmate.ui.chat_room.drawer

import umc.onairmate.data.model.entity.ParticipantData

interface ParticipantItemClickListener {
    fun clickMessage(data: ParticipantData)

    // 새로 추가할 항목
    fun clickReport(data: ParticipantData)
    fun clickRecommend(data: ParticipantData)
    fun clickAddFriend(data: ParticipantData)
    fun clickBlock(data: ParticipantData)
}