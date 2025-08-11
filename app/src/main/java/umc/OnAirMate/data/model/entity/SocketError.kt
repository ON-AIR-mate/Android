package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class SocketError(
    @SerializedName("type")
    val type: String,
    @SerializedName("message")
    val message : String
)
