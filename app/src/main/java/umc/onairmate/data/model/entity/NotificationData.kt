package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class NotificationData(
    @SerializedName("today")
    val today : List<String>,
    @SerializedName("yesterday")
    val yesterday : List<String>,
    @SerializedName("recentDays")
    val recentDays : List<String>

)
