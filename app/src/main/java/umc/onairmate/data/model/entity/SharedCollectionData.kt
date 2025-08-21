package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class SharedCollectionData(
    @SerializedName("sharedCollectionId")
    val sharedCollectionId: Int,
    @SerializedName("originalCollectionId")
    val originalCollectionId: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("fromUserId")
    val fromUserId: Int,
    @SerializedName("fromUserNickname")
    val fromUserNickname: String,
    @SerializedName("bookmarkCount")
    val bookmarkCount: Int,
    @SerializedName("sharedAt")
    val sharedAt: String
)