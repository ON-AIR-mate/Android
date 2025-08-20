package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class NotificationData(
    @SerializedName("notificationId")
    val notificationId : Int = 0,
    @SerializedName("type")
    val type : String = "",
    @SerializedName("message")
    val message : String = "",
    @SerializedName("isRead")
    val isRead : Boolean = false,
    @SerializedName("createdAt")
    val createdAt : String = "",
    @SerializedName("fromUserId")
    val fromUserId : Int = 0,
    @SerializedName("fromUserNickname")
    val fromUserNickname : String = ""
)