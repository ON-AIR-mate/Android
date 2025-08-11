package umc.onairmate.data.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import umc.onairmate.data.model.entity.CollectionData
import umc.onairmate.data.model.entity.CollectionDetailData
import umc.onairmate.data.model.request.CreateCollectionRequest
import umc.onairmate.data.model.response.CreateCollectionResponse
import umc.onairmate.data.model.response.RawDefaultResponse

interface CollectionService {

    // 새로운 컬렉션 생성
    @POST("collections")
    suspend fun createCollection(
        @Header("Authorization") accessToken: String,
        @Body body: CreateCollectionRequest
    ): RawDefaultResponse<CreateCollectionResponse>

    // 사용자의 컬렉션 목록 조회
    @GET("collections")
    suspend fun getCollections(
        @Header("Authorization") accessToken: String
    ): RawDefaultResponse<List<CollectionData>>

    // 특정 컬렉션의 상세 정보 및 북마크 조회
    @GET("collections/{collectionId}")
    suspend fun getCollectionDetailInfo(
        @Header("Authorization") accessToken: String
    ): RawDefaultResponse<CollectionDetailData>
}