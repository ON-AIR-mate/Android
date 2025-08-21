package umc.onairmate.ui.lounge.personal
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.repository.repository.SharedCollectionsRepository
import umc.onairmate.data.model.entity.SharedCollectionData
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.api.SharedCollectionsService

import javax.inject.Inject

@HiltViewModel
class SharedCollectionsViewModel @Inject constructor(
    private val repository: SharedCollectionsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val TAG = this.javaClass.simpleName

    // 토큰 받아오기용
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // 뷰모델 내부에서는 MutableLiveData로 데이터 수정
    // LiveData는 외부(액티비티 등)에서 읽기전용 -> 둘이 세트로 구현
    private val _sharedCollections = MutableLiveData<List<SharedCollectionData>>()
    val sharedCollections: LiveData<List<SharedCollectionData>> get() = _sharedCollections

    // 친구의 공개 컬렉션 리스트 LiveData (새로 추가)
    private val _friendPublicCollections = MutableLiveData<List<CollectionData>>()
    val friendPublicCollections: LiveData<List<CollectionData>> get() = _friendPublicCollections

    // UX 향상을 위해 서버가 로딩 중임을 프로그레스바로 표시할 때 사용 예정
    // api 호출 시 true, 응답이 오면 false로 한다 정도로만 생각해주세요
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // 토큰 읽어오기
    fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    // 공유 받은 컬렉션 리스트 받아오기
    fun getSharedCollections() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()

            if (token != null) {
                val result = repository.getSharedCollections(token)
                Log.d(TAG, "getSharedCollections api 호출")

                when (result) {
                    is DefaultResponse.Success -> {
                        Log.d(TAG, "응답 성공: ${result.data}")
                        _sharedCollections.postValue(result.data)
                    }
                    is DefaultResponse.Error -> {
                        Log.e(TAG, "에러: ${result.code} - ${result.message}")
                        // 에러 시 빈 리스트 또는 에러 처리 로직 추가
                        _sharedCollections.postValue(emptyList())
                    }
                }
            } else {
                Log.e(TAG, "토큰이 없습니다.")
                // 토큰이 없을 경우 처리 로직
                _sharedCollections.postValue(emptyList())
            }

            _isLoading.value = false
        }
    }

    // 친구의 공개 컬렉션 정보를 받아오는 함수
            fun getFriendPublicCollections() {
                viewModelScope.launch {
                    _isLoading.value = true
                    val token = getToken()

                    // 토큰이 null이 아닐 때만 API를 호출합니다.
                    if (token != null) {
                        val result = repository.getFriendPublicCollections(token)
                        Log.d(TAG, "getFriendPublicCollections api 호출")

                        when (result) {
                            is DefaultResponse.Success -> {
                                Log.d(TAG, "응답 성공: ${result.data}")
                                // _friendPublicCollections LiveData에 데이터를 할당합니다.
                                _friendPublicCollections.postValue(result.data)
                            }

                            is DefaultResponse.Error -> {
                                Log.e(TAG, "에러: ${result.code} - ${result.message}")
                                _friendPublicCollections.value = emptyList()
                            }
                        }
                    } else {
                        // 토큰이 null일 경우, 에러를 로그에 기록하고 빈 리스트를 할당합니다.
                        Log.e(TAG, "토큰이 없습니다.")
                        _friendPublicCollections.value = emptyList()
                    }

                    _isLoading.value = false
                }
            }



}