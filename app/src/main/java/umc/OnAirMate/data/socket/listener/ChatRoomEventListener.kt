package umc.onairmate.data.socket.listener

import umc.onairmate.data.model.entity.ChatMessageData

interface ChatRoomEventListener {
    fun onNewChat(chatMessage: ChatMessageData) {}
    fun onError(errorMessage: String ){}
    fun onUserJoined(data : String){}
    fun onUserLeft(data: Int){}
}