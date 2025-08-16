package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class RoomWithBookmarkCreateResponse(
    @SerializedName("roomId")
    val roomId : Int,
    @SerializedName("message")
    val message : String,
    @SerializedName("thumbnail")
    val thumbnail: String
)
