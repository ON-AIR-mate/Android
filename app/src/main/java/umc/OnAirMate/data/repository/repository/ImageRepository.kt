package umc.onairmate.data.repository.repository

import okhttp3.MultipartBody
import umc.onairmate.data.model.response.ImageResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface ImageRepository {
    suspend fun uploadImage(body : MultipartBody.Part): RawDefaultResponse<ImageResponse>
}