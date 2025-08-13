package umc.onairmate.data.model.request

import com.google.gson.annotations.SerializedName

data class FriendRequest(
    @SerializedName("targetUserId")
    val targetUserId : Int
)
