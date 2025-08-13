package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class RoomSettingData(
    @SerializedName("autoArchiving")
    val autoArchiving: Boolean = true,
    @SerializedName("invitePermission")
    val invitePermission: String = InvitePermission.MANAGER_ONLY.label,
    @SerializedName("isPrivate")
    val isPrivate: Boolean = false,
    @SerializedName("maxParticipants")
    val maxParticipants: Int = ParticipantPreset.EIGHT.count
)

enum class InvitePermission(val apiName, val displayName: String) {
    ALL("ALL","모두 허용"),
    HOST("HOST","방장만 허용");

    companion object {
        fun fromApiName(apiName: String): InvitePermission? = entries.find { it.label == label }
    }
}

enum class ParticipantPreset(val count: Int) {
    EIGHT(8),
    FIFTEEN(15),
    THIRTY(30);

    companion object {
        fun fromCount(count: Int): ParticipantPreset? = entries.find { it.count == count }
    }
}