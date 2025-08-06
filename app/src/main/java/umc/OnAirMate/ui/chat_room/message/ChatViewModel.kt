package umc.onairmate.ui.chat_room.message

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import org.json.JSONObject
import umc.onairmate.data.model.entity.ChatMessageData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.repository.repository.ChatRoomRepository
import umc.onairmate.data.socket.SocketDispatcher
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.data.socket.handler.ChatHandler
import umc.onairmate.data.socket.listener.ChatEventListener
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRoomRepository,
    @ApplicationContext private val context: Context
) : ViewModel(), ChatEventListener {

    private val TAG = javaClass.simpleName
    private val spf = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _chatHistory = MutableLiveData<List<ChatMessageData>>(emptyList())
    val chatHistory: LiveData<List<ChatMessageData>> get() = _chatHistory

    private val _chat = MutableLiveData<ChatMessageData>()
    val chat: LiveData<ChatMessageData> get() = _chat

    init {
        val chatHandler = ChatHandler(this)
        SocketDispatcher.register("NEW_MESSAGE", chatHandler)
        SocketDispatcher.register("BOOKMARK_CREATED", chatHandler)
        Log.d(TAG, "connect chatRoom")
    }

    override fun onNewMessage(data: ChatMessageData) {
        _chat.postValue( data)
    }
    fun getToken(): String? {
        return spf.getString("access_token", null)
    }
    // 초기 메시지 로드
    fun getChatHistory(roomId: Int) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }
            val result = repository.getChatHistory(token,roomId)
            Log.d(TAG, "getChatHistory api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _chatHistory.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _chatHistory.postValue(emptyList())
                }
            }
        }
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