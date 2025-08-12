package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class SocketMessage(
    @SerializedName("type")
    val type: String,
    @SerializedName("message")
    val message : String
)
