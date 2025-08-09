package umc.onairmate.data.model.request

import com.google.gson.annotations.SerializedName


data class AcceptFriendBody (
    @SerializedName("action")
    val action : String
)