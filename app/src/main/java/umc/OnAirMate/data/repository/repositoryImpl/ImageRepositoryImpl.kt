package umc.onairmate.data.repository.repositoryImpl

import okhttp3.MultipartBody
import umc.onairmate.data.api.ImageService
import umc.onairmate.data.model.response.ImageResponse
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.repository.repository.ImageRepository

class ImageRepositoryImpl(
    private val api : ImageService
) : ImageRepository{
    override suspend fun uploadImage(body: MultipartBody.Part): RawDefaultResponse<ImageResponse> {
        return api.uploadImage(body)
    }
}