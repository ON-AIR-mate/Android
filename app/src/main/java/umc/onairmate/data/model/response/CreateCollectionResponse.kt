package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class CreateCollectionResponse(
    @SerializedName("collectionId")
    val collectionId: Int,
    @SerializedName("message")
    val message: String
)
