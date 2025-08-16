package umc.onairmate.data.model.request

import com.google.gson.annotations.SerializedName

data class RoomWithBookmarkCreateRequest(
    @SerializedName("roomTitle")
    val roomTitle : String,
    @SerializedName("maxParticipants")
    val maxParticipants : Int,
    @SerializedName("isPrivate")
    val isPrivate : Boolean,
    @SerializedName("startFrom")
    val startFrom : String
)

enum class RoomStartOption(val apiName: String, val displayName: String) {
    BOOKMARK("BOOKMARK", "북마크 시간부터"),
    BEGINNING("BEGINNING", "영상 처음부터");

    companion object {
        fun fromApi(apiName: String) = entries.find { it.apiName.equals(apiName, ignoreCase = true) }
        fun fromDisplayName(displayName: String) = entries.find { it.displayName.equals(displayName, ignoreCase = true) }
    }
}
