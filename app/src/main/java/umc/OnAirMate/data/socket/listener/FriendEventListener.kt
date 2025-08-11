package umc.onairmate.data.socket.listener

import umc.onairmate.data.model.entity.DirectMessageData
import umc.onairmate.data.model.entity.SocketError

interface FriendEventListener {
    fun onNewDirectMessage(directMessage: DirectMessageData?){}
    fun onError(errorMessage: SocketError?){}

}