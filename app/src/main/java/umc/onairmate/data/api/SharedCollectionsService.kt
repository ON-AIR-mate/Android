package umc.onairmate.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.model.entity.SharedCollectionData

interface SharedCollectionsService {
    @GET("shared-collections")
    suspend fun getSharedCollection (
        @Header("Authorization") accessToken: String
    ) : RawDefaultResponse<List<SharedCollectionData>>
}