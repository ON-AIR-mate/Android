package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class ChatMessageData(
    @SerializedName("messageId")
    val messageId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("profileImage")
    val profileImage: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("messageType")
    val messageType: String,
    @SerializedName("timestamp")
    val timestamp: String
)
