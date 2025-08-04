package umc.onairmate.data.socket.listener

import umc.onairmate.data.model.entity.ChatMessageData

interface ChatEventListener {
    fun onNewMessage(data: ChatMessageData) {}
    fun onBookmarkCreated(data: ChatMessageData) {}
}