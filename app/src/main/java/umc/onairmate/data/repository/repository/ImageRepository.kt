package umc.onairmate.data.repository.repository

import okhttp3.MultipartBody
import umc.onairmate.data.model.request.ProfileRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.ImageResponse
import umc.onairmate.data.model.response.MessageResponse

interface ImageRepository {
    suspend fun uploadImage( body : MultipartBody.Part): DefaultResponse<ImageResponse>
    suspend fun editProfile(accessToken: String, body: ProfileRequest): DefaultResponse<MessageResponse>
}