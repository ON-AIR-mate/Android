package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class ReadCountResponse(
    @SerializedName("unreadCount")
    val unreadCount: Int
)
