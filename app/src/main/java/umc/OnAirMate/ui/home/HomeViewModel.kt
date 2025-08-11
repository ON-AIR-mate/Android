package umc.onairmate.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.entity.RoomData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.RoomListResponse
import umc.onairmate.data.repository.repository.HomeRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HomeRepository,
    @ApplicationContext private val context: Context
) : ViewModel(){
    private val TAG = this.javaClass.simpleName
    private val spf = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _roomData = MutableLiveData<List<RoomData>>()
    val roomData : LiveData<List<RoomData>> get() = _roomData

    private val _roomDetailInfo = MutableLiveData<RoomData?>()
    val roomDetailInfo : LiveData<RoomData?> get() = _roomDetailInfo

    private val _roomListResponse = MutableLiveData<RoomListResponse>()
    val roomListResponse : LiveData<RoomListResponse> get() = _roomListResponse

    private val _recommendedVideo = MutableLiveData<List<String>>()
    val recommendedVideo : LiveData<List<String>> get() = _recommendedVideo

    private val _joinRoom = MutableLiveData<Boolean>()
    val joinRoom : LiveData<Boolean> get() = _joinRoom

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getToken(): String? {
        return spf.getString("access_token", null)
    }

    // 테스트용 더미데이터 생성
    private fun getDummyRoom(n : Int) : List<RoomData>{
        val dummy = arrayListOf<RoomData>()
        for(i in 0.. n) dummy.add(
            RoomData(
            roomId = i,
            roomTitle = "dummy ${i}",
            videoTitle = "",
            videoThumbnail = "",
            hostNickname = "host${i}",
            hostProfileImage = "",
            currentParticipants = i,
            maxParticipants = 10,
            duration = "00:45:20"
            )
        )
        return dummy
    }
    private fun getDummyString(n : Int) : List<String>{
        val dummy = arrayListOf<String>()
        for(i in 0.. n) dummy.add("DummyData ${i}")
        return dummy
    }


    // 방 목록 가져오기
    fun getRoomList(sortBy : String, searchType : String, keyword : String){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.getRoomList(token, sortBy, searchType, keyword)
            Log.d(TAG, "getRoomList api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _roomListResponse.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _roomData.value = getDummyRoom(5)
                }
            }
            _isLoading.value = false
        }
    }

    fun getRoomDetailInfo(roomId : Int){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.getRoomDetailInfo(token, roomId)
            Log.d(TAG, "getRoomDetailInfo api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _roomDetailInfo.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                }
            }
            _isLoading.value = false
        }
    }
    fun clearRoomDetailInfo(){
        _roomDetailInfo.value=null
    }

   fun joinRoom( roomId : Int){
       viewModelScope.launch {
           _isLoading.value = true
           val token = getToken()
           if (token == null) {
               Log.e(TAG, "토큰이 없습니다")
               _isLoading.value = false
               return@launch
           }
           val result = repository.joinRoom(token, roomId)
           Log.d(TAG, "joinRoom api 호출")
           when (result) {
               is DefaultResponse.Success -> {
                   Log.d(TAG,"응답 성공 : ${result.data}")
                   _joinRoom.value = true

               }
               is DefaultResponse.Error -> {
                   Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                   _joinRoom.value = false
               }
           }
           _isLoading.value = false
       }
   }
   fun leaveRoom(roomId : Int){
       viewModelScope.launch {
           _isLoading.value = true
           val token = getToken()
           if (token == null) {
               Log.e(TAG, "토큰이 없습니다")
               _isLoading.value = false
               return@launch
           }
           val result = repository.leaveRoom(token, roomId)
           Log.d(TAG, "leaveRoom api 호출")
           when (result) {
               is DefaultResponse.Success -> {
                   Log.d(TAG,"응답 성공 : ${result.data}")

               }
               is DefaultResponse.Error -> {
                   Log.e(TAG, "에러: ${result.code} - ${result.message} ")
               }
           }
           _isLoading.value = false
       }
   }

    // 추천영상 가져오기
    fun getRecommendedVideo(){
        _recommendedVideo.value = getDummyString(5)
    }

}
