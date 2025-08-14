package umc.onairmate.data.model.request

import com.google.gson.annotations.SerializedName

data class FriendInviteRequest (
    @SerializedName("roomId")
    val roomId: Int
)