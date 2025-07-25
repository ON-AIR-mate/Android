package umc.onairmate.ui.friend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import umc.onairmate.data.FriendData
import umc.onairmate.data.RequestedFriendData
import javax.inject.Inject

class FriendViewModel @Inject constructor(

) : ViewModel(){
    private val TAG = this.javaClass.simpleName

    private val _friendList = MutableLiveData<List<FriendData>>()
    val friendList : LiveData<List<FriendData>> get() = _friendList

    private val _requestedFriendList = MutableLiveData<List<RequestedFriendData>>()
    val requestedFriendList : LiveData<List<RequestedFriendData>> get() = _requestedFriendList

    private fun initDummyFriend() : List<FriendData>{
        val dummy = arrayListOf<FriendData>()
        for(i in 1..5){
            val online = if (i%2 == 0 ) true else false
            dummy.add(FriendData(i,"friend${i}","",0,online))
        }
        return dummy
    }
    private fun initDummyRequest() : List<RequestedFriendData>{
        val dummy = arrayListOf<RequestedFriendData>()
        for(i in 1..5){
            dummy.add(RequestedFriendData(i,i,"request${i}","",0,"time"))
        }
        return dummy
    }

    fun getFriendList(){
        Log.d(TAG,"getFriendList")
        _friendList.value = initDummyFriend()
    }

    fun getRequestedFriendList(){
        Log.d(TAG,"getRequestList")
        _requestedFriendList.value = initDummyRequest()
    }


}