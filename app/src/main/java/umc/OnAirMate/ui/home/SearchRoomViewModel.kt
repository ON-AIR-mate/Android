package umc.onairmate.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import umc.onairmate.data.RoomData
import javax.inject.Inject

class SearchRoomViewModel @Inject constructor(

) : ViewModel(){
    private val TAG = this.javaClass.simpleName

    private val _roomData = MutableLiveData<List<RoomData>>()
    val roomData : LiveData<List<RoomData>> get() = _roomData

    private val _testData = MutableLiveData<List<String>>()
    val testData : LiveData<List<String>> get() = _testData

    private val _recommendedVideo = MutableLiveData<List<String>>()
    val recommendedVideo : LiveData<List<String>> get() = _recommendedVideo


    // 테스트용 더미데이터 생성
    private fun getDummyRoom(n : Int) : List<RoomData>{
        val dummy = arrayListOf<RoomData>()
        for(i in 0.. n) dummy.add(RoomData(
            roomId = i,
            roomTitle = "dummy ${i}",
            videoTitle = "",
            videoThumbnail = null,
            hostNickname = "host${i}",
            hostProfileImage = null,
            currentParticipants = i,
            maxParticipants = 10,
            duration = "00:45:20"
        ))
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
        if(type == 0) _roomData.value = emptyList<RoomData>()
        else _roomData.value = getDummyRoom(5)
    }

    // 추천영상 가져오기
    fun getRecommendedVideo(){
        _recommendedVideo.value = getDummyString(5)
    }

}