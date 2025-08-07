package umc.onairmate.data.socket.listener

import umc.onairmate.data.model.entity.DirectMessageData

interface FriendEventListener {
    fun onNewDirectMessage(directMessage: DirectMessageData){}
    fun onError(errorMessage: String ){}

}