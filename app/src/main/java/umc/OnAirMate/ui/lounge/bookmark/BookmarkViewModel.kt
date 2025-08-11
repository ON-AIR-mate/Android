package umc.onairmate.ui.lounge.bookmark

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.request.CreateBookmarkRequest
import umc.onairmate.data.model.request.CreateRoomWithBookmarkRequest
import umc.onairmate.data.model.request.MoveCollectionRequest
import umc.onairmate.data.model.response.BookmarkListResponse
import umc.onairmate.data.model.response.CreateBookmarkResponse
import umc.onairmate.data.model.response.CreateRoomWithBookmarkResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.repository.repository.BookmarkRepository
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repository: BookmarkRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val TAG = this.javaClass.simpleName

    // token
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)


    // 만들어진 북마크 정보
    private val _createdBookmarkDataInfo = MutableLiveData<CreateBookmarkResponse>()
    val createdBookmarkDataInfo : LiveData<CreateBookmarkResponse> get() = _createdBookmarkDataInfo

    // 사용자의 북마크 목록
    private val _bookmarkList = MutableLiveData<BookmarkListResponse>()
    val bookmarkList : LiveData<BookmarkListResponse> get() = _bookmarkList

    // 북마크 삭제 여부 확인
    private val _isBookmarkDeleted = MutableLiveData<MessageResponse>()
    val isBookmarkDeleted : LiveData<MessageResponse> get() = _isBookmarkDeleted

    // 북마크 컬렉션 변경 여부 확인
    private val _isBookmarkMoved = MutableLiveData<MessageResponse>()
    val isBookmarkMoved : LiveData<MessageResponse> get() = _isBookmarkMoved

    // 생성된 방 정보
    private val _createdRoomDataInfo = MutableLiveData<CreateRoomWithBookmarkResponse>()
    val createdRoomDataInfo : LiveData<CreateRoomWithBookmarkResponse> get() = _createdRoomDataInfo

    // 서버 로딩중 - 프로그래스바
    // api호출시 true, 응답이 오면 false
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    // 북마크 생성
    fun createBookmark(bookmarkCreateData: CreateBookmarkRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.createBookmark(token, bookmarkCreateData)
            Log.d(TAG, "createBookmark api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d("응답 성공", "${result.data}")
                    _createdBookmarkDataInfo.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d("에러", "${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }
    }

    /**
     * getBookmarks: 북마크 목록 조회
     * - collectionId: 검색하고 싶은 컬렉션의 id
     * - uncategorized: 컬렉션이 지정되지 않은 북마크만 조회할지 여부
     * - 조회 후 받는 데이터는 uncategorized와 all로 구성되어 있음
     * - uncategorized와 all에 속한 데이터는 서로 중복이 없음
     * - collectionId=null / uncategorized=false 면 사용자의 전체 북마크를 조회함
     */
    fun getBookmarks(collectionId: Int?, uncategorized: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.getBookmarks(token, collectionId, uncategorized)
            Log.d(TAG, "getBookmarks api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d("응답 성공", "${result.data}")
                    _bookmarkList.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d("에러", "${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }
    }

    // 북마크 삭제
    fun deleteBookmark(bookmarkId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.deleteBookmark(token, bookmarkId)
            Log.d(TAG, "deleteBookmark api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d("응답 성공", "${result.data}")
                    _isBookmarkDeleted.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d("에러", "${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }
    }

    // 북마크 컬렉션 이동
    fun moveCollectionOfBookmark(bookmarkId: Int, collectionData: MoveCollectionRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.moveCollectionOfBookmark(token, bookmarkId, collectionData)
            Log.d(TAG, "moveCollectionOfBookmark api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d("응답 성공", "${result.data}")
                    _isBookmarkMoved.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d("에러", "${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }
    }

    // 북마크로 방 생성
    fun createRoomWithBookmark(bookmarkId: Int, roomSettingData: CreateRoomWithBookmarkRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.createRoomWithBookmark(token, bookmarkId, roomSettingData)
            Log.d(TAG, "createRoomWithBookmark api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d("응답 성공", "${result.data}")
                    _createdRoomDataInfo.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d("에러", "${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }
    }
}