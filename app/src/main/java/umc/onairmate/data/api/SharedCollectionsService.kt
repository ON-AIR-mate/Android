package umc.onairmate.data.api

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.model.entity.SharedCollectionData
import umc.onairmate.data.model.entity.CollectionData

interface SharedCollectionsService {
    @GET("shared-collections")
    suspend fun getSharedCollection (
        @Header("Authorization") accessToken: String
    ) : RawDefaultResponse<List<SharedCollectionData>>

    @GET("friends/{userId}/lounge")
    suspend fun  getFriendPublicCollections(
        @Header("Authorization") accessToken: String,
        @Path("userId") userId: Int
    ) :  RawDefaultResponse<List<CollectionData>>


}