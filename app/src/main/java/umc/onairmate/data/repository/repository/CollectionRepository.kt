package umc.onairmate.data.repository.repository

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.request.CreateCollectionRequest
import umc.onairmate.data.model.request.ModifyCollectionRequest
import umc.onairmate.data.model.request.ShareCollectionRequest
import umc.onairmate.data.model.response.CollectionListResponse
import umc.onairmate.data.model.response.CreateCollectionResponse
import umc.onairmate.data.model.response.DefaultResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse

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
        accessToken: String,
        collectionId: Int
    ): DefaultResponse<CollectionDetailData>

    // 컬렉션을 친구에게 공유하기
    suspend fun shareCollection(
        accessToken: String,
        collectionId: Int,
        body: ShareCollectionRequest
    ): DefaultResponse<MessageResponse>

    // 컬렉션 수정
    suspend fun modifyCollection(
        accessToken: String,
        collectionId: Int,
        body: ModifyCollectionRequest
    ): DefaultResponse<MessageResponse>

    // 컬렉션 삭제
    suspend fun deleteCollection(
        accessToken: String,
        collectionId: Int,
    ): DefaultResponse<MessageResponse>
}