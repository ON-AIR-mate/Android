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
import umc.onairmate.data.socket.SocketManager
import umc.onairmate.data.socket.handler.ChatRoomHandler
import umc.onairmate.data.socket.listener.ChatRoomEventListener
import javax.inject.Inject

@HiltViewModel
class VideoChatViewModel @Inject constructor(
    private val repository: ChatRoomRepository,
    @ApplicationContext private val context: Context
) : ViewModel(), ChatRoomEventListener {

    private val TAG = javaClass.simpleName
    private val spf = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private var handler: ChatRoomHandler = ChatRoomHandler(this)

    private val _chatHistory = MutableLiveData<List<ChatMessageData>>(emptyList())
    val chatHistory: LiveData<List<ChatMessageData>> get() = _chatHistory

    private val _chat = MutableLiveData<ChatMessageData>()
    val chat: LiveData<ChatMessageData> get() = _chat

    fun getToken(): String? {
        return spf.getString("access_token", null)
    }
    fun getHandler(): ChatRoomHandler = handler

    override fun onNewChat(data: ChatMessageData) {
        Log.d(TAG,"onNewChat : ${data}")
        _chat.postValue( data)
    }

    override fun onUserJoined(data: String) {
        Log.d(TAG,"onUserJoined : ${data}")
    }

    override fun onError(message: String) {
        Log.d(TAG,"error ${message}")
    }

    override fun onUserLeft(data: Int) {
        Log.d(TAG,"onUserLeft ${data}")
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


    fun sendMessage(roomId: Int, nickname: String,content: String) {
        if (content.isBlank()) return

        Log.d(TAG,"채팅 입력 ${content}")
        val json = JSONObject().apply {
            put("roomId", roomId)
            put("nickname", nickname)
            put("content", content)
            put("messageType", "general")
        }

        SocketManager.getSocket().emit("sendRoomMessage", json)
    }

    fun joinRoom(roomId: Int,nickname: String, isVisited: Boolean = false){
        Log.d(TAG,"joinRoom ${roomId}")
        val type = if(isVisited) "enterRoom" else "joinRoom"
        val json = JSONObject().apply {
            put("roomId", roomId)
            put("nickname", nickname)
        }
        SocketManager.getSocket().emit(type, json)
    }

    fun leaveRoom(roomId: Int){
        Log.d(TAG,"joinRoom ${roomId}")
        val json = JSONObject().apply {
            put("roomId", roomId)
        }
        SocketManager.getSocket().emit("leaveRoom", json)
    }



    override fun onCleared() {
        super.onCleared()
        SocketManager.unregisterHandler(handler)
    }
}