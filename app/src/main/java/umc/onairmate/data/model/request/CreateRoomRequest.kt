package umc.onairmate.data.model.request

import com.google.gson.annotations.SerializedName

data class CreateRoomRequest(
    @SerializedName("roomName")
    val roomName : String,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("isPrivate")
    val isPrivate : Boolean,
    @SerializedName("videoId")
    val videoId : String
)
