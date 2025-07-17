package umc.onairmate.data

import com.google.gson.annotations.SerializedName

data class RoomData(
    @SerializedName("roomId")
    val roomId : Int = 0,
    @SerializedName("roomTitle")
    val roomTitle : String = "",
    @SerializedName("videoTitle")
    val videoTitle: String = "",
    @SerializedName("videoThumbnail")
    val videoThumbnail : String?,
    @SerializedName("hostNickname")
    val hostNickname : String = "",
    @SerializedName("hostProfileImage")
    val hostProfileImage : String?,
    @SerializedName("hostPopularity")
    val hostPopularity : Int = 0,
    @SerializedName("currentParticipants")
    val currentParticipants : Int = 0,
    @SerializedName("maxParticipants")
    val maxParticipants : Int = 0,
    @SerializedName("duration")
    val duration : String = "",
    @SerializedName("isPrivate")
    val  isPrivate : Boolean = true
)
