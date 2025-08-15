package umc.onairmate.data.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookmarkData(
    @SerializedName("bookmarkId")
    val bookmarkId: Int,
    @SerializedName("message")
    val message: String,
    @SerializedName("timeline")
    val timeline: Int
): Parcelable