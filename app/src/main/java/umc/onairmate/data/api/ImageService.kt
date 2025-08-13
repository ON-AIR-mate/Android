package umc.onairmate.data.api

import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import umc.onairmate.data.model.response.ImageResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface ImageService {

    @Multipart
    @POST("users/profile/image")
    suspend fun uploadImage(
        @Header("Authorization") accessToken: String,
        @Part profileImage : MultipartBody.Part
    ): RawDefaultResponse<ImageResponse>
}