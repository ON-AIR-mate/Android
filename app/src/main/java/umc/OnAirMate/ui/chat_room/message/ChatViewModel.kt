package umc.onairmate.ui.chat_room.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.json.JSONObject
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.socket.SocketDispatcher
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.data.socket.handler.ChatHandler
import umc.onairmate.data.socket.listener.ChatEventListener
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel(), ChatEventListener {

    private val _messages = MutableLiveData<List<ChatMessageData>>(emptyList())
    val messages: LiveData<List<ChatMessageData>> get() = _messages

    init {
        val chatHandler = ChatHandler(this)
        SocketDispatcher.register("NEW_MESSAGE", chatHandler)
        SocketDispatcher.register("BOOKMARK_CREATED", chatHandler)
    }

    override fun onNewMessage(data: ChatMessageData) {
        _messages.postValue(_messages.value.orEmpty() + data)
    }

    override fun onBookmarkCreated(data: ChatMessageData) {
        // 북마크 메시지 처리 로직 (필요 시 추가)
    }

    fun sendMessage(roomId: Int, content: String) {
        if (content.isBlank()) return

        val json = JSONObject().apply {
            put("type", "SEND_MESSAGE")
            put("data", JSONObject().apply {
                put("roomId", roomId)
                put("content", content)
                put("messageType", "GENERAL")
            })
        }
        SocketManager.emit("SEND_MESSAGE", json)
    }

    override fun onCleared() {
        super.onCleared()
        SocketDispatcher.unregister("NEW_MESSAGE")
        SocketDispatcher.unregister("BOOKMARK_CREATED")
    }
}