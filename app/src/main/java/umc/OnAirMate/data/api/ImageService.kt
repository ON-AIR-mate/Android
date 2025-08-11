package umc.onairmate.data.api

import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.POST
import umc.onairmate.data.model.response.ImageResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface ImageService {
    @POST("users/profile/image")
    suspend fun uploadImage(
        @Body body : MultipartBody.Part
    ): RawDefaultResponse<ImageResponse>
}