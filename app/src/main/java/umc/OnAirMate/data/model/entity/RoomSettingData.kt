package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class RoomSettingData(
    @SerializedName("autoArchiving")
    val autoArchiving: Boolean,
    @SerializedName("invitePermission")
    val invitePermission: String,
    @SerializedName("isPrivate")
    val isPrivate: Boolean,
    @SerializedName("maxParticipants")
    val maxParticipants: Int
)