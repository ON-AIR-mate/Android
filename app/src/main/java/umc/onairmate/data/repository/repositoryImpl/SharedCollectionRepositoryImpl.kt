package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.SharedCollectionsService
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.SharedCollectionData
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.repository.repository.SharedCollectionsRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class SharedCollectionRepositoryImpl @Inject constructor (
    private val api : SharedCollectionsService
) :SharedCollectionsRepository{
    override suspend fun getSharedCollections(accessToken: String): DefaultResponse<List<SharedCollectionData>> {
        return safeApiCall { api.getSharedCollection(accessToken) }
    }

    override suspend fun getFriendPublicCollections(accessToken: String): DefaultResponse<List<CollectionData>> {
        return safeApiCall { api.getFriendPublicCollections(accessToken) }
    }
}