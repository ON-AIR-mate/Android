package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class DirectMessageData (
    @SerializedName("senderId")
    val senderId : Int,
    @SerializedName("receiverId")
    val receiverId : Int,
    @SerializedName("content")
    val content : String,
    @SerializedName("messageType")
    val messageType : String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName ("roomInvite")
    val roomInvite : InviteMessageData?,
    @SerializedName ("collection")
    val collection : String?
)