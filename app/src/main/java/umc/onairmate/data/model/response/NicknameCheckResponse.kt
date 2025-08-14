package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class NicknameCheckResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("data")
    val data: NicknameData,
    val isAvailable: Boolean?
)

data class NicknameData(
    @SerializedName("available")
    val available: Boolean,
    @SerializedName("message")
    val message: String
)