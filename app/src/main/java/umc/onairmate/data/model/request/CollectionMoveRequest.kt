package umc.onairmate.data.model.request

import com.google.gson.annotations.SerializedName

data class CollectionMoveRequest(
    @SerializedName("collectionId")
    val collectionId: Int?
)
