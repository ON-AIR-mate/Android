package umc.onairmate.ui.home.video

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.entity.VideoData
import umc.onairmate.data.model.request.CreateRoomRequest
import umc.onairmate.data.model.response.CreateRoomResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.repository.repository.HomeRepository
import javax.inject.Inject

@HiltViewModel
class SearchVideoViewModel @Inject constructor(
    private val repository: HomeRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val TAG = this.javaClass.simpleName
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // 영상 검색
    private val _searchedVideos = MutableLiveData<List<VideoData>>()
    val searchedVideos : LiveData<List<VideoData>> get() = _searchedVideos

    // 영상 상세 정보
    private val _videoDetailInfo = MutableLiveData<VideoData?>()
    val videoDetailInfo : LiveData<VideoData?> get() = _videoDetailInfo

    // 추천 영상 검색
    private val _recommendedVideos = MutableLiveData<List<VideoData>>()
    val recommendedVideos : LiveData<List<VideoData>> get() = _recommendedVideos

    // 만들어진 방 id
    private val _createdRoomInfo = MutableLiveData<CreateRoomResponse>()
    val createdRoomInfo : LiveData<CreateRoomResponse> get() = _createdRoomInfo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    fun createRoom(body: CreateRoomRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.createRoom(token, body)
            Log.d(TAG, "create room api 호출")

            when(result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "create room 응답 성공: ${result.data}")
                    _createdRoomInfo.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "create room 에러: ${result.code} - ${result.message} ")
                }
            }
            _isLoading.value = false
        }
    }

    fun searchVideoList(query: String, limit: Int) {
        viewModelScope.launch {
            // 빈 문자열 검사
            if (query.isBlank()) {
                _searchedVideos.postValue(emptyList())
                return@launch
            }

            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.searchVideoList(token, query, limit)
            Log.d(TAG, "searchVideoList api 호출")

            when(result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "video search 응답 성공: ${result.data}")
                    _searchedVideos.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "video search 에러: ${result.code} - ${result.message} ")
                }
            }

            _isLoading.value = false
        }
    }

    fun getVideoDetailInfo(videoId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.getVideoDetailInfo(token, videoId)
            Log.d(TAG, "getVideoDetailInfo api 호출")

            when(result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "video detail 응답 성공: ${result.data}")
                    _videoDetailInfo.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "video detail 에러: ${result.code} - ${result.message} ")
                }
            }

            _isLoading.value = false
        }
    }

    fun setVideoDetailInfo(data: VideoData) {
        _videoDetailInfo.value = data
    }

    fun clearVideoDetailInfo(){
        _videoDetailInfo.value=null
    }

    fun getRecommendVideoList(keyword: String, limit: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.getRecommendVideoList(token, keyword, limit)
            Log.d(TAG, "getRecommendVideoList api 호출")

            when(result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "recommend video 응답 성공: ${result.data}")
                    _recommendedVideos.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "recommend video 에러: ${result.code} - ${result.message} ")
                }
            }

            _isLoading.value = false
        }
    }

}