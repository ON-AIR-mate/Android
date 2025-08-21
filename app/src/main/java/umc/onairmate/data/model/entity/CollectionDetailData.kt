package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CollectionDetailData(
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
    val visibility: String,
    @SerializedName("rooms")
    val rooms: List<RoomArchiveData>
): Serializable
