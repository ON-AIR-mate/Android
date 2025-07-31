package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

// 단순 message만 받는 response
data class MessageResponse (
    @SerializedName("message")
    val message : String
)