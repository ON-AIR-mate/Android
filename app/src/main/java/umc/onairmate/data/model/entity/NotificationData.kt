package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class NotificationData(
    @SerializedName("notificationId")
    val notificationId : Int,
    @SerializedName("type")
    val type : String,
    @SerializedName("message")
    val message : String,
    @SerializedName("isRead")
    val isRead : Boolean,
    @SerializedName("createdAt")
    val createdAt : String,
    @SerializedName("fromUserId")
    val fromUserId : Int,
    @SerializedName("fromUserNickname")
    val fromUserNickname : String
)