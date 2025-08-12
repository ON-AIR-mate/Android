package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CollectionData(
    @SerializedName("bookmarkCount")
    val bookmarkCount: Int,
    @SerializedName("collectionId")
    val collectionId: Int,
    @SerializedName("coverImage")
    val coverImage: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("updatedAt")
    val updatedAt: String,
    @SerializedName("visibility")
    val visibility: String
): Serializable

enum class CollectionVisibility(val apiName: String, val displayName: String) {
    PRIVATE("PRIVATE", "비공개"),
    FRIENDS_ONLY("FRIENDS_ONLY", "친구만 공개"),
    PUBLIC("PUBLIC", "전체공개");

    companion object {
        // apiName 기반으로 enum 상수 검색
        fun fromApiName(apiName: String): CollectionVisibility? {
            return entries.find { it.apiName.equals(apiName, ignoreCase = true) }
        }
        // displayName을 기반으로 enum 상수 검색
        fun fromDisplayName(displayName: String): CollectionVisibility? {
            return entries.find { it.displayName.equals(displayName, ignoreCase = true) }
        }
    }
}