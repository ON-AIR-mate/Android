package umc.onairmate.data.repository.repositoryImpl

import okhttp3.MultipartBody
import umc.onairmate.data.api.ImageService
import umc.onairmate.data.model.request.ProfileRequest
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.ImageResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.repository.repository.ImageRepository
import umc.onairmate.data.util.safeApiCall

class ImageRepositoryImpl(
    private val api : ImageService
) : ImageRepository{
    override suspend fun uploadImage( body: MultipartBody.Part): DefaultResponse<ImageResponse> {
        return safeApiCall {  api.uploadImage(body) }
    }

    override suspend fun editProfile(
        accessToken: String,
        body: ProfileRequest
    ): DefaultResponse<MessageResponse> {
        return safeApiCall { api.editProfile(accessToken, body) }
    }
}