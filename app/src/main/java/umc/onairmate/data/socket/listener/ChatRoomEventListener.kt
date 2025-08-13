package umc.onairmate.data.socket.listener

import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.SocketError

interface ChatRoomEventListener {
    fun onNewChat(chatMessage: ChatMessageData) {}
    fun onError(errorMessage: SocketError){}
    fun onUserJoined(isSuccess : Boolean){}
    fun onUserLeft(isSuccess: Boolean){}
    fun onRoomSettingsUpdated(data: RoomData){}
}