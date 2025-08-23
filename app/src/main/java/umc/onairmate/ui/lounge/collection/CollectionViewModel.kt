package umc.onairmate.ui.lounge.collection

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.request.CollectionCreateRequest
import umc.onairmate.data.model.request.CollectionModifyRequest
import umc.onairmate.data.model.request.CollectionShareRequest
import umc.onairmate.data.model.response.CollectionListResponse
import umc.onairmate.data.model.response.CollectionCreateResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.ImportResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.repository.repository.CollectionRepository
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val repository: CollectionRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val TAG = this.javaClass.simpleName

    // token
    private val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    // 만들어진 북마크 정보
    private val _createdCollectionDataInfo = MutableLiveData<CollectionCreateResponse>()
    val createdCollectionDataInfo : LiveData<CollectionCreateResponse> get() = _createdCollectionDataInfo

    // 사용자의 컬렉션 리스트
    private val _collectionList = MutableLiveData<CollectionListResponse>()
    val collectionList : LiveData<CollectionListResponse> get() = _collectionList

    // 컬렉션 세부 정보
    private val _collectionDetailDataInfo = MutableLiveData<CollectionDetailData>()
    val collectionDetailDataInfo : LiveData<CollectionDetailData> get() = _collectionDetailDataInfo

    // 공유 성공 여부
    private val _shareCollectionMessage = MutableLiveData<MessageResponse>()
    val shareCollectionMessage : LiveData<MessageResponse> get() = _shareCollectionMessage

    // 수정 성공 여부
    private val _modifyCollectionMessage = MutableLiveData<MessageResponse>()
    val modifyCollectionMessage : LiveData<MessageResponse> get() = _modifyCollectionMessage

    // 삭제 성공 여부
    private val _deleteCollectionMessage = MutableLiveData<MessageResponse>()
    val deleteCollectionMessage : LiveData<MessageResponse> get() = _deleteCollectionMessage

    // 친구 컬렉션 가져오기
    private val _importResponse = MutableLiveData<ImportResponse>()
    val importResponse: LiveData<ImportResponse> get() = _importResponse


    // 서버 로딩중 - 프로그래스바
    // api호출시 true, 응답이 오면 false
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private fun getToken(): String? {
        return sharedPreferences.getString("access_token", null)
    }

    // 컬렉션 생성
    fun createCollection(collectionCreateData: CollectionCreateRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.createCollection(token, collectionCreateData)
            Log.d(TAG, "createCollection api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답성공: ${result.data}")
                    _createdCollectionDataInfo.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "에러: ${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }
    }

    // 사용자 전체 컬렉션 가져오기
    fun getCollections() {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.getCollections(token)
            Log.d(TAG, "getCollections api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답성공: ${result.data}")
                    _collectionList.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "에러: ${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }
    }

    // 컬렉션 내부 정보 (북마크 리스트 포함) 가져오기
    fun getCollectionDetailInfo(collectionId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.getCollectionDetailInfo(token, collectionId)
            Log.d(TAG, "getCollectionDetailInfo api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답성공: ${result.data}")
                    _collectionDetailDataInfo.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "에러: ${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }
    }

    // 컬렉션 공유하기
    fun shareCollection(collectionId: Int, request: CollectionShareRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.shareCollection(token, collectionId, request)
            Log.d(TAG, "shareCollection api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답성공: ${result.data}")
                    _shareCollectionMessage.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "에러: ${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }
    }

    fun modifyCollection(collectionId: Int, request: CollectionModifyRequest) {
        viewModelScope.launch {
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }

            val result = repository.modifyCollection(token, collectionId, request)
            Log.d(TAG, "modifyCollection api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답성공: ${result.data}")
                    _modifyCollectionMessage.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "에러: ${result.code} - ${result.message}")
                }
            }
        }
    }

    fun deleteCollection(collectionId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }

            val result = repository.deleteCollection(token, collectionId)
            Log.d(TAG, "deleteCollection api 호출")

            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG, "응답성공: ${result.data}")
                    _deleteCollectionMessage.postValue(result.data)
                }
                is DefaultResponse.Error -> {
                    Log.d(TAG, "에러: ${result.code} - ${result.message}")
                }
            }

            _isLoading.value = false
        }
    }

    // 친구 컬렉션 가져오기
    fun importToMyCollection(collectionId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                _isLoading.value = false
                return@launch
            }
            if (token != null) {
                val result = repository.importToMyCollection(token, collectionId)
                Log.d(TAG, "importToMyCollection API 호출")
                when (result) {
                    is DefaultResponse.Success -> {
                        Log.d(TAG, "컬렉션 가져오기 성공: ${result.data}")
                        _importResponse.postValue(result.data)
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
}