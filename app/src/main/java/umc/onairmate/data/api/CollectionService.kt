package umc.onairmate.data.api

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.request.CollectionCreateRequest
import umc.onairmate.data.model.request.CollectionModifyRequest
import umc.onairmate.data.model.request.CollectionShareRequest
import umc.onairmate.data.model.response.CollectionListResponse
import umc.onairmate.data.model.response.CollectionCreateResponse
import umc.onairmate.data.model.response.MessageResponse
import umc.onairmate.data.model.response.RawDefaultResponse
import umc.onairmate.data.model.response.ImportResponse
import umc.onairmate.data.model.request.ShareRequest
interface CollectionService {

    // 새로운 컬렉션 생성
    @POST("collections")
    suspend fun createCollection(
        @Header("Authorization") accessToken: String,
        @Body body: CollectionCreateRequest
    ): RawDefaultResponse<CollectionCreateResponse>

    // 사용자의 컬렉션 목록 조회
    @GET("collections")
    suspend fun getCollections(
        @Header("Authorization") accessToken: String
    ): RawDefaultResponse<CollectionListResponse>

    // 특정 컬렉션의 상세 정보 및 북마크 조회
    @GET("collections/{collectionId}")
    suspend fun getCollectionDetailInfo(
        @Header("Authorization") accessToken: String,
        @Path("collectionId") collectionId: Int,
    ): RawDefaultResponse<CollectionDetailData>

    // 컬렉션을 친구에게 공유하기
    @POST("collections/{collectionId}/share")
    suspend fun shareCollection(
        @Header("Authorization") accessToken: String,
        @Path("collectionId") collectionId: Int,
        @Body body: CollectionShareRequest
    ): RawDefaultResponse<MessageResponse>

    // 컬렉션 수정
    @PUT("collections/{collectionId}")
    suspend fun modifyCollection(
        @Header("Authorization") accessToken: String,
        @Path("collectionId") collectionId: Int,
        @Body body: CollectionModifyRequest
    ): RawDefaultResponse<MessageResponse>

    // 컬렉션 삭제
    @DELETE("collections/{collectionId}")
    suspend fun deleteCollection(
        @Header("Authorization") accessToken: String,
        @Path("collectionId") collectionId: Int,
    ): RawDefaultResponse<MessageResponse>

    // 컬렉션을 친구에게 공유하기
    @POST("collections/{collectionId}/share")
    suspend fun shareToMyCollection(
        @Header("Authorization") accessToken: String,
        @Path("collectionId") collectionId: Int,
        @Body request: ShareRequest
    ): RawDefaultResponse<MessageResponse>

    // 타인의 컬렉션을 "내 라운지"로 가져오기
    @POST("collections/{collectionId}/copy")
    suspend fun importToMyCollection(
        @Header("Authorization") accessToken: String,
        @Path("collectionId") collectionId: Int,
    ): RawDefaultResponse<ImportResponse>
}