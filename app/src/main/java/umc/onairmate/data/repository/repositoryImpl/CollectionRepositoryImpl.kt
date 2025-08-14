package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.CollectionService
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.request.CreateCollectionRequest
import umc.onairmate.data.model.request.ModifyCollectionRequest
import umc.onairmate.data.model.request.ShareCollectionRequest
import umc.onairmate.data.model.response.CollectionListResponse
import umc.onairmate.data.model.response.CreateCollectionResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.repository.repository.CollectionRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val api: CollectionService
): CollectionRepository {

    override suspend fun createCollection(
        accessToken: String,
        body: CreateCollectionRequest
    ): DefaultResponse<CreateCollectionResponse> {
        return safeApiCall {
            api.createCollection(accessToken, body)
        }
    }

    override suspend fun getCollections(accessToken: String): DefaultResponse<CollectionListResponse> {
        return safeApiCall {
            api.getCollections(accessToken)
        }
    }

    override suspend fun getCollectionDetailInfo(accessToken: String, collectionId: Int): DefaultResponse<CollectionDetailData> {
        return safeApiCall {
            api.getCollectionDetailInfo(accessToken, collectionId)
        }
    }

    override suspend fun shareCollection(accessToken: String, collectionId: Int, body: ShareCollectionRequest): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.shareCollection(accessToken, collectionId, body)
        }
    }

    override suspend fun modifyCollection(
        accessToken: String,
        collectionId: Int,
        body: ModifyCollectionRequest
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.modifyCollection(accessToken, collectionId, body)
        }
    }

    override suspend fun deleteCollection(
        accessToken: String,
        collectionId: Int
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.deleteCollection(accessToken, collectionId)
        }
    }

}