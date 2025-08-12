package umc.onairmate.data.repository.repository

import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.request.CreateCollectionRequest
import umc.onairmate.data.model.response.CollectionListResponse
import umc.onairmate.data.model.response.CreateCollectionResponse
import umc.onairmate.data.model.response.DefaultResponse

interface CollectionRepository {

    // 새로운 컬렉션 생성
    suspend fun createCollection(
        accessToken: String,
        body: CreateCollectionRequest
    ): DefaultResponse<CreateCollectionResponse>

    // 사용자의 컬렉션 목록 조회
    suspend fun getCollections(
        accessToken: String
    ): DefaultResponse<CollectionListResponse>

    // 특정 컬렉션의 상세 정보 및 북마크 조회
    suspend fun getCollectionDetailInfo(
        accessToken: String
    ): DefaultResponse<CollectionDetailData>
}