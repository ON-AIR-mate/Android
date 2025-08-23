package umc.onairmate.data.repository.repository

import retrofit2.http.GET
import retrofit2.http.Header
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.SharedCollectionData
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.model.response.DefaultResponse

interface SharedCollectionsRepository {

    suspend fun getSharedCollections (
        accessToken: String
    ) : DefaultResponse<List<SharedCollectionData>>

    suspend fun  getFriendPublicCollections(
        accessToken: String,
        userId : Int
    ) : DefaultResponse<List<CollectionData>>

}