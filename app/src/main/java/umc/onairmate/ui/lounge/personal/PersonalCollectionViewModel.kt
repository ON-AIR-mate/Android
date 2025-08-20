package umc.onairmate.ui.lounge.personal

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import umc.onairmate.data.repository.repository.CollectionRepository
import umc.onairmate.data.model.request.ShareRequest
import umc.onairmate.data.model.response.ImportResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.FriendData
import umc.onairmate.data.repository.repository.FriendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository,
    private val friendRepository: FriendRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val TAG = this.javaClass.simpleName

    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // 컬렉션 공유 API 응답을 위한 LiveData
    private val _shareResponse = MutableLiveData<DefaultResponse<MessageResponse>>()
    val shareResponse: LiveData<DefaultResponse<MessageResponse>> get() = _shareResponse

    // 컬렉션 가져오기 API 응답을 위한 LiveData
    private val _importResponse = MutableLiveData<DefaultResponse<ImportResponse>>()
    val importResponse: LiveData<DefaultResponse<ImportResponse>> get() = _importResponse

    // 로딩 상태를 위한 LiveData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // 컬렉션 목록 LiveData
    private val _collections = MutableLiveData<List<CollectionData>>()
    val collections: LiveData<List<CollectionData>> = _collections

    // 친구 목록 LiveData
    private val _friends = MutableLiveData<List<FriendData>>()
    val friends: LiveData<List<FriendData>> = _friends

    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    // 컬렉션 공유 함수
    fun shareToMyCollection(collectionId: Int, friendIds: List<Int>) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()

            if (token != null) {
                val request = ShareRequest(friendIds)
                val result = collectionRepository.shareToMyCollection(token, collectionId, request)
                Log.d(TAG, "shareToMyCollection API 호출")

                _shareResponse.postValue(result)

                when (result) {
                    is DefaultResponse.Success -> {
                        Log.d(TAG, "컬렉션 공유 성공: ${result.data}")
                    }
                    is DefaultResponse.Error -> {
                        Log.e(TAG, "컬렉션 공유 실패: ${result.code} - ${result.message}")
                    }
                }
            } else {
                Log.e(TAG, "토큰이 없습니다.")
                // 토큰이 없을 경우의 오류 처리
            }

            _isLoading.value = false
        }
    }

    // 컬렉션 가져오기 함수
    fun importToMyCollection(collectionId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()

            if (token != null) {
                val result = collectionRepository.importToMyCollection(token, collectionId)
                Log.d(TAG, "importToMyCollection API 호출")

                _importResponse.postValue(result)

                when (result) {
                    is DefaultResponse.Success -> {
                        Log.d(TAG, "컬렉션 가져오기 성공: ${result.data}")
                    }
                    is DefaultResponse.Error -> {
                        Log.e(TAG, "컬렉션 가져오기 실패: ${result.code} - ${result.message}")
                    }
                }
            } else {
                Log.e(TAG, "토큰이 없습니다.")
                // 토큰이 없을 경우의 오류 처리
            }

            _isLoading.value = false
        }
    }

    // 컬렉션 목록을 가져오는 함수
    fun fetchCollections() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()

            if (token != null) {
                // TODO: 컬렉션 리포지토리의 컬렉션 가져오기 API 함수 호출
                val result = collectionRepository.getCollections(token)
                // ⭐️ 응답이 성공(Success)인 경우에만 데이터를 처리
                when (result) {
                    is DefaultResponse.Success -> {
                        // result.data는 CollectionListResponse 타입입니다.
                        // 이 객체 안에 컬렉션 리스트가 담겨 있습니다.
                        // ⭐️ collectionList라는 필드에 리스트가 있다고 가정
                        _collections.postValue(result.data.collections)
                    }
                    is DefaultResponse.Error -> {
                        Log.e(TAG, "컬렉션 가져오기 실패: ${result.message}")
                        // 오류 발생 시 빈 리스트를 전달하거나 null 처리
                        _collections.postValue(emptyList())
                    }
                }
            } else {
                Log.e(TAG, "토큰이 없습니다.")
            }
            _isLoading.value = false
        }
    }

    // 친구 목록을 가져오는 함수
    fun fetchFriends() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()

            if (token != null) {
                val result = friendRepository.getFriendList(token)

                when (result) {
                    is DefaultResponse.Success -> {
                        // ⭐️ FriendListResponse 객체에서 리스트 필드명을 찾아 직접 접근
                        _friends.postValue(result.data)
                    }
                    is DefaultResponse.Error -> {
                        Log.e(TAG, "친구 목록 가져오기 실패: ${result.message}")
                        _friends.postValue(emptyList())
                    }
                }
            } else {
                Log.e(TAG, "토큰이 없습니다.")
                _friends.postValue(emptyList())
            }

            _isLoading.value = false
        }
    }

}