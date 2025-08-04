package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class ChatMessageData(
    @SerializedName("roomId")
    val roomId : Int,
    @SerializedName("content")
    val content  : String,
    @SerializedName("messageType")
    val messageType : String
)
