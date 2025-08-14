package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class DirectMessageData (
    @SerializedName("messageId")
    val messageId : Int,
    @SerializedName("senderId")
    val senderId : Int,
    @SerializedName("receiverId")
    val receiverId : Int,
    @SerializedName("content")
    val content : String,
    @SerializedName("messageType")
    val messageType : String,
    @SerializedName("timestamp")
    val timestamp: String
)