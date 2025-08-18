package umc.onairmate.data.model.response

import com.google.gson.annotations.SerializedName

data class BookmarkCreateResponse(
    @SerializedName("bookmarkId")
    val bookmarkId: Int,
    @SerializedName("message")
    val message: String
)
