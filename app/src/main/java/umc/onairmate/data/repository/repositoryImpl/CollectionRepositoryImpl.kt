package umc.onairmate.data.repository.repositoryImpl

import umc.onairmate.data.api.CollectionService
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.request.CollectionCreateRequest
import umc.onairmate.data.model.request.CollectionModifyRequest
import umc.onairmate.data.model.request.CollectionShareRequest
import umc.onairmate.data.model.response.CollectionListResponse
import umc.onairmate.data.model.response.CollectionCreateResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.repository.repository.CollectionRepository
import umc.onairmate.data.util.safeApiCall
import javax.inject.Inject

class CollectionRepositoryImpl @Inject constructor(
    private val api: CollectionService
): CollectionRepository {

    // 새로운 컬렉션 생성
    override suspend fun createCollection(
        accessToken: String,
        body: CollectionCreateRequest
    ): DefaultResponse<CollectionCreateResponse> {
        return safeApiCall {
            api.createCollection(accessToken, body)
        }
    }

    // 사용자의 컬렉션 목록 조회
    override suspend fun getCollections(accessToken: String): DefaultResponse<CollectionListResponse> {
        return safeApiCall {
            api.getCollections(accessToken)
        }
    }

    // 특정 컬렉션의 상세 정보 및 북마크 조회
    override suspend fun getCollectionDetailInfo(accessToken: String, collectionId: Int): DefaultResponse<CollectionDetailData> {
        return safeApiCall {
            api.getCollectionDetailInfo(accessToken, collectionId)
        }
    }

    // 컬렉션을 친구에게 공유하기
    override suspend fun shareCollection(accessToken: String, collectionId: Int, body: CollectionShareRequest): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.shareCollection(accessToken, collectionId, body)
        }
    }

    // 컬렉션 수정
    override suspend fun modifyCollection(
        accessToken: String,
        collectionId: Int,
        body: CollectionModifyRequest
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.modifyCollection(accessToken, collectionId, body)
        }
    }

    // 컬렉션 삭제
    override suspend fun deleteCollection(
        accessToken: String,
        collectionId: Int
    ): DefaultResponse<MessageResponse> {
        return safeApiCall {
            api.deleteCollection(accessToken, collectionId)
        }
    }

}