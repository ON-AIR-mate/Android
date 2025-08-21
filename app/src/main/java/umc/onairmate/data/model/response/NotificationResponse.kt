package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName
import umc.onairmate.data.model.entity.NotificationData

data class NotificationResponse(
    @SerializedName("today")
    val today : List<NotificationData> = emptyList(),
    @SerializedName("yesterday")
    val yesterday : List<NotificationData> = emptyList() ,
    @SerializedName("recentDays")
    val recentDays : List<NotificationData> = emptyList()

)