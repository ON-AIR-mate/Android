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

    override fun onNewDirectMessage(directMessage: DirectMessageData?) {
        viewModelScope.launch(Dispatchers.Main) {
            Log.d(TAG,"onNewDirectMessage : ${directMessage}")
            _generalChat.postValue(directMessage!!)

        }
    }
    override fun onError(error: SocketError?) {
        viewModelScope.launch(Dispatchers.Main) {
            Log.d(TAG,"error ${error!!.type} : ${error.message}")
        }
    }

    fun joinDM(receiverId: Int){
        Log.d(TAG,"joinDM ${receiverId}")
        val json = JSONObject().apply {
            put("receiverId", receiverId)
        }
        SocketManager.emit("joinDM", json)
    }

    fun sendMessage(receiverId: Int, fromNickname: String,content: String) {
        if (content.isBlank()) return

        Log.d(TAG,"채팅 입력 ${content}")
        val json = JSONObject().apply {
            put("receiverId", receiverId)
            //put("fromNickname", fromNickname)
            put("content", content)
            put("messageType", "general")
        }

        SocketManager.emit("sendDirectMessage", json)
    }
}