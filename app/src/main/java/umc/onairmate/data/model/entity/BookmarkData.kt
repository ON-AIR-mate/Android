package umc.onairmate.data.model.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookmarkData(
    @SerializedName("bookmarkId")
    val bookmarkId: Int,
    @SerializedName("collectionTitle")
    val collectionTitle: String? = null,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("timeline")
    val timeline: Int,
    @SerializedName("videoThumbnail")
    val videoThumbnail: String,
    @SerializedName("videoTitle")
    val videoTitle: String
): Parcelable