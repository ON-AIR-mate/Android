package umc.onairmate.data.socket.listener

import umc.onairmate.data.model.entity.ChatMessageData

interface ChatRoomEventListener {
    fun onNewChat(data: ChatMessageData) {}
    fun onError(message: String ){}
    fun onUserJoined(data : String){}
    fun onUserLeft(data: Int){}
}