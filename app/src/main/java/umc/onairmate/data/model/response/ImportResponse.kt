package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class ImportResponseData(
    @SerializedName("collectionId")
    val collectionId: Int
)

data class ImportResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: ImportResponseData,
    @SerializedName("message")
    val message: String
)