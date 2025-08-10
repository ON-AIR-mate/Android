package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class MoveCollectionResponse(
    @SerializedName("collectionId")
    val collectionId: Int?
)
