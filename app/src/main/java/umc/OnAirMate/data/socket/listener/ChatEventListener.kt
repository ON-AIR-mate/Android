package umc.onairmate.data.socket.listener

import umc.onairmate.data.model.entity.ChatMessageData

interface ChatEventListener {
    fun onNewChat(data: ChatMessageData) {}
    fun onBookmarkCreated(data: ChatMessageData) {}
    fun onError(message: String ){}
    fun onUserJoined(data : String){}
}