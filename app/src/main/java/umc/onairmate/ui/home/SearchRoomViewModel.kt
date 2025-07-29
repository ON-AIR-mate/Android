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
import umc.onairmate.data.model.response.RoomListResponse
import umc.onairmate.data.repository.repository.HomeRepository
import javax.inject.Inject

@HiltViewModel
class SearchRoomViewModel @Inject constructor(
    private val repository: HomeRepository,
    @ApplicationContext private val context: Context
) : ViewModel(){
    private val TAG = this.javaClass.simpleName
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _roomData = MutableLiveData<List<RoomData>>()
    val roomData : LiveData<List<RoomData>> get() = _roomData

    private val _roomDetailInfo = MutableLiveData<RoomData>()
    val roomDetailInfo : LiveData<RoomData> get() = _roomDetailInfo

    private val _roomListResponse = MutableLiveData<RoomListResponse>()
    val roomListResponse : LiveData<RoomListResponse> get() = _roomListResponse

    private val _recommendedVideo = MutableLiveData<List<String>>()
    val recommendedVideo : LiveData<List<String>> get() = _recommendedVideo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    // 테스트용 더미데이터 생성
    private fun getDummyRoom(n : Int) : List<RoomData>{
        val dummy = arrayListOf<RoomData>()
        for(i in 0.. n) dummy.add(
            RoomData(
            roomId = i,
            roomTitle = "dummy ${i}",
            videoTitle = "",
            videoThumbnail = null,
            hostNickname = "host${i}",
            hostProfileImage = null,
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
    // type : 0 -> 빈 데이터 테스트 / 1 -> 데이터 있는 경우 테스트
    fun getRoomList(type : Int ){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            try{
                val response  = repository.getRoomList(token)
                if(response.success){
                    Log.d(TAG, "getRoomList 응답 성공: ${response!!}")
                    _roomListResponse.postValue(response.data!!)
                }
                else Log.d(TAG, "getRoomList 응답 실패: ${response.error}")
            }catch (e: Exception){
                Log.d(TAG, "getRoomList api 요청 실패: ${e}")
            }finally {
                _isLoading.value = false
            }
        }
        if(type == 0) _roomData.value = emptyList<RoomData>()
        else _roomData.value = getDummyRoom(5)
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
            try{
                val response  = repository.getRoomInfo(token, roomId)
                if(response.success){
                    Log.d(TAG, "getRoomDetailInfo 응답 성공: ${response}")
                    _roomDetailInfo.postValue(response.data!!)
                }
                else Log.d(TAG, "getRoomDetailInfo 응답 실패: ${response.error}")
            }catch (e: Exception){
                Log.d(TAG, "getRoomDetailInfo api 요청 실패: ${e}")
            }finally {
                _isLoading.value = false
            }
        }
    }

    // 추천영상 가져오기
    fun getRecommendedVideo(){
        _recommendedVideo.value = getDummyString(5)
    }

}