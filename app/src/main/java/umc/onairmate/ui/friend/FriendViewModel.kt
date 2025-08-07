package umc.onairmate.ui.friend

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.model.entity.RequestedFriendData
import umc.onairmate.data.model.entity.UserData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.repository.repository.FriendRepository
import javax.inject.Inject


@HiltViewModel
class FriendViewModel @Inject constructor(
    private val repository: FriendRepository,
    @ApplicationContext private val context: Context
) : ViewModel(){
    private val TAG = this.javaClass.simpleName
    private val spf = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _friendList = MutableLiveData<List<FriendData>>()
    val friendList : LiveData<List<FriendData>> get() = _friendList

    private val _requestedFriendList = MutableLiveData<List<RequestedFriendData>>()
    val requestedFriendList : LiveData<List<RequestedFriendData>> get() = _requestedFriendList

    private val _searchedUserList =  MutableLiveData<List<UserData>>()
    val searchedUserList : LiveData<List<UserData>> get() = _searchedUserList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _result = MutableLiveData<String?>()
    val result: LiveData<String?> = _result

    fun getToken(): String? {
        return spf.getString("access_token", null)
    }

    private fun initDummyFriend() : List<FriendData>{
        val dummy = arrayListOf<FriendData>()
        for(i in 1..20)
            dummy.add(FriendData(19,"이현서","",0,false))
        return dummy
    }
    private fun initDummyRequest() : List<RequestedFriendData>{
        val dummy = arrayListOf<RequestedFriendData>()
        for(i in 1..5){
            dummy.add(RequestedFriendData(i,i,"request${i}","",0,"time"))
        }
        return dummy
    }

    fun clearResult(){
        _result.value=null
    }

    fun getFriendList(){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.getFriendList(token)
            Log.d(TAG, "getFriendList api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _friendList.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _friendList.postValue(emptyList())
                }
            }
            _isLoading.value = false
        }

    }

    fun getRequestedFriendList(){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.getRequestedFriendList(token)
            Log.d(TAG, "getRequestedFriendList api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _requestedFriendList.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _requestedFriendList.postValue(emptyList<RequestedFriendData>())
                }
            }
            _isLoading.value = false
        }
    }

    fun searchUser(nickname: String){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.searchUser(token,nickname)
            Log.d(TAG, "searchUser api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _searchedUserList.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _searchedUserList.postValue(emptyList<UserData>())
                }
            }
            _isLoading.value = false
        }
    }

    fun requestFriend(userId : Int){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.requestFriend(token,userId)
            Log.d(TAG, "requestFriend api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _result.postValue(result.data.message)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _result.postValue(result.message)
                }
            }
            _isLoading.value = false
        }
    }

    fun acceptFriend(userId : Int, action: String){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.acceptFriend(token,userId, action)
            Log.d(TAG, "acceptFriend api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _result.postValue(result.data.message)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _result.postValue(result.message)
                }
            }
            _isLoading.value = false
        }
    }

    fun deleteFriend(userId : Int){
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.deleteFriend(token,userId)
            Log.d(TAG, "deleteFriend api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _result.postValue(result.data.message)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _result.postValue(result.message)
                }
            }
            _isLoading.value = false
        }
    }

    fun inviteFriend(userId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            val result = repository.inviteFriend(token,userId)
            Log.d(TAG, "inviteFriend api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _result.postValue(result.data.message)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _result.postValue(result.message)
                }
            }
            _isLoading.value = false
        }
    }

}