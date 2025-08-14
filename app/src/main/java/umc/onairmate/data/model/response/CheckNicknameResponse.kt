package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class CheckNicknameResponse (
    @SerializedName("available")
    val available : Boolean,
    @SerializedName("message")
    val message : String
)