package umc.onairmate.data.socket.listener

import umc.onairmate.data.model.entity.DirectMessageData
import umc.onairmate.data.model.entity.SocketMessage

interface FriendEventListener {
    fun onNewDirectMessage(directMessage: DirectMessageData){}
    fun onError(errorMessage: SocketMessage){}

}