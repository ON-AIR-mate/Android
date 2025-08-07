package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class JoinProfileResponse (
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message : String
)