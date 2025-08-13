package umc.onairmate.ui

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.repository.repository.ImageRepository
import umc.onairmate.ui.util.UriToMultipartUtil
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val repository: ImageRepository,
    @ApplicationContext private val context: Context
): ViewModel() {

    private val TAG = this.javaClass.simpleName
    private val spf = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    private val _imageUrl= MutableLiveData<String>()
    val imageUrl: LiveData<String> = _imageUrl

    private fun getToken(): String? {
        return spf.getString("access_token", null)
    }

    fun uploadUri(uri: Uri){
        viewModelScope.launch {

            val token = getToken()
            if (token == null) {
                Log.e(TAG, "토큰이 없습니다")
                return@launch
            }
            val part = UriToMultipartUtil.uriToMultipart(context, uri, "profileImage")
            val result = repository.uploadImage(token, part)
            Log.d(TAG, "uploadUri api 호출")
            when (result) {
                is DefaultResponse.Success -> {
                    Log.d(TAG,"응답 성공 : ${result.data}")
                    _imageUrl.value = result.data.profileImage
                }
                is DefaultResponse.Error -> {
                    Log.e(TAG, "에러: ${result.code} - ${result.message} ")
                    _imageUrl.value = ""
                }
            }
        }
    }
}