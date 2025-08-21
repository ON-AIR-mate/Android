package umc.onairmate.data.model.entity

import com.google.gson.annotations.SerializedName

data class ParticipatedRoomData(
    @SerializedName("roomId")
    val roomId: Int,
    @SerializedName("roomTitle")
    val roomTitle: String,
    @SerializedName("videoTitle")
    val videoTitle: String,
    @SerializedName("videoThumbnail")
    val videoThumbnail: String,
    @SerializedName("participatedAt")
    val participatedAt: String,
    @SerializedName("bookmarks")
    val bookmarks: List<BookmarkData>
)