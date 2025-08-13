package umc.onairmate.ui.friend.chat

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import umc.onairmate.data.model.entity.DirectMessageData
import umc.onairmate.data.model.entity.SocketError
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.data.socket.handler.FriendHandler
import umc.onairmate.data.socket.listener.FriendEventListener
import javax.inject.Inject

@HiltViewModel
class FriendChatViewModel @Inject constructor(
    @ApplicationContext private val context: Context
): ViewModel(), FriendEventListener {

    private val TAG = javaClass.simpleName
    private val spf = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _generalChat = MutableLiveData<DirectMessageData>()
    val generalChat: LiveData<DirectMessageData> get() = _generalChat

    private val handler: FriendHandler = FriendHandler(this)

    fun getHandler(): FriendHandler = handler

    companion object{
        const val GENERAL_MESSAGE = "general"
        const val INVITE_MESSAGE = "roomInvite"
        const val COLLECTION_MESSAGE = "collectionShare"
    }

    override fun onNewDirectMessage(directMessage: DirectMessageData) {
        viewModelScope.launch(Dispatchers.Main) {
            Log.d(TAG,"onNewDirectMessage : ${directMessage}")
            _generalChat.postValue(directMessage)

        }
    }
    override fun onError(errorMessage: SocketError) {
        viewModelScope.launch(Dispatchers.Main) {
            Log.d(TAG,"error ${errorMessage.type} : ${errorMessage.message}")
        }
    }

    fun joinDM(receiverId: Int){
        Log.d(TAG,"joinDM ${receiverId}")
        val json = JSONObject().apply {
            put("receiverId", receiverId)
        }
        SocketManager.emit("joinDM", json)
    }

    fun sendMessage(receiverId: Int,fromNickname: String,  content: String) {
        if (content.isBlank()) return
        val json = JSONObject().apply {
            put("receiverId", receiverId)
<<<<<<< HEAD
            //put("fromNickname", fromNickname)
=======
>>>>>>> main
            put("content", content)
            put("fromNickname", fromNickname)

            put("messageType",GENERAL_MESSAGE )
        }

        SocketManager.emit("sendDirectMessage", json)
    }

    fun deleteFriend(receiverId: Int, senderId: Int){
        val json = JSONObject().apply {
            put("userId1", receiverId)
            put("userId2", senderId)
        }
        SocketManager.emit("noFriend", json)
    }
}