package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class CollectionCreateResponse(
    @SerializedName("collectionId")
    val collectionId: Int,
    @SerializedName("message")
    val message: String
)
