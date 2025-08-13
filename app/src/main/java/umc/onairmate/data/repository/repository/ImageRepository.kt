package umc.onairmate.data.repository.repository

import okhttp3.MultipartBody
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.ImageResponse

interface ImageRepository {
    suspend fun uploadImage(accessToken: String, body : MultipartBody.Part): DefaultResponse<ImageResponse>
}