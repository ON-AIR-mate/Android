package umc.onairmate.data.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class InviteMessageData (
    @SerializedName("inviter")
    val inviter : UserData,
    @SerializedName("room")
    val room : RoomData,
    @SerializedName("video")
    val video: VideoData,
    @SerializedName("invitedAt")
    val invitedAt : String
)