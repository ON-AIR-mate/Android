package umc.onairmate.data.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookmarkRoomData(
    @SerializedName("collectionTitle")
    val collectionTitle: String?,
    @SerializedName("roomId")
    val roomId: Int,
    @SerializedName("roomName")
    val roomName: String,
    @SerializedName("videoThumbnail")
    val videoThumbnail: String,
    @SerializedName("videoTitle")
    val videoTitle: String
): Parcelable