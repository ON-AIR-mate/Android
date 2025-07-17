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

    private fun getDummyRoom(n : Int) : List<RoomData>{
        val dummy = arrayListOf<RoomData>()
        for(i in 0.. n) dummy.add(RoomData(i))
        return dummy
    }
    private fun getDummyString(n : Int) : List<String>{
        val dummy = arrayListOf<String>()
        for(i in 0.. n) dummy.add("DummyData ${i}")
        return dummy
    }

    fun getRoomList(type : Int ){
        if(type == 0) _roomData.value = emptyList<RoomData>()
        else _roomData.value = getDummyRoom(5)
    }

    fun getRecommendedVideo(){
        _recommendedVideo.value = getDummyString(5)
    }

}