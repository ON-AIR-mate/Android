package umc.onairmate.data.socket.listener

import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.entity.SocketMessage
import umc.onairmate.data.model.entity.UserLeftData

interface ChatRoomEventListener {
    fun onNewChat(chatMessage: ChatMessageData) {}
    fun onError(errorMessage: SocketMessage){}
    fun onSuccess(successMessage : SocketMessage){}
    fun onUserJoined(isSuccess : Boolean){}
    fun onUserLeft(userLeftData: UserLeftData){}
    fun onRoomSettingsUpdated(data: RoomData){}
}