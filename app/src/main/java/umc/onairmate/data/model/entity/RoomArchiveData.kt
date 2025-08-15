package umc.onairmate.data.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomArchiveData(
    @SerializedName("bookmarks")
    val bookmarks: List<BookmarkData?>,
    @SerializedName("roomData")
    val roomData: BookmarkRoomData
): Parcelable