package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class CreateRoomResponse (
    @SerializedName("roomId")
    val roomId : Int,
    @SerializedName("message")
    val message : String
)