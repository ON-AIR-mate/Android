package umc.onairmate.ui.lounge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import umc.onairmate.ui.lounge.model.Collection

class LoungeCollectionViewModel : ViewModel() {

    // 내부에서 수정 가능한 MutableLiveData
    private val _collectionList = MutableLiveData<List<Collection>>(emptyList())

    // 외부에 노출할 LiveData
    val collectionList: LiveData<List<Collection>> get() = _collectionList

    // 컬렉션 추가 함수
    fun addCollection(collection: Collection) {
        val updatedList = _collectionList.value?.toMutableList() ?: mutableListOf()
        updatedList.add(0, collection) // 최신순 정렬 위해 맨 앞에 추가
        _collectionList.value = updatedList
    }
}
